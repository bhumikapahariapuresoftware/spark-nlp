/*
 * Copyright 2017-2022 John Snow Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.johnsnowlabs.nlp.embeddings

import com.johnsnowlabs.nlp.annotators.Tokenizer
import com.johnsnowlabs.nlp.base.DocumentAssembler
import com.johnsnowlabs.nlp.util.io.{ReadAs, ResourceHelper}
import com.johnsnowlabs.tags.{FastTest, SlowTest}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.scalatest.flatspec.AnyFlatSpec

class WordEmbeddingsTestSpec extends AnyFlatSpec {

  "Word Embeddings" should "correctly embed clinical words not embed non-existent words" taggedAs SlowTest in {

    val words = ResourceHelper.spark.read
      .option("header", "true")
      .csv("src/test/resources/embeddings/clinical_words.txt")
    val notWords = ResourceHelper.spark.read
      .option("header", "true")
      .csv("src/test/resources/embeddings/not_words.txt")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("word")
      .setOutputCol("document")

    val tokenizer = new Tokenizer()
      .setInputCols(Array("document"))
      .setOutputCol("token")

    val embeddings = WordEmbeddingsModel
      .pretrained()
      .setInputCols("document", "token")
      .setOutputCol("embeddings")
      .setCaseSensitive(false)

    val pipeline = new Pipeline()
      .setStages(Array(documentAssembler, tokenizer, embeddings))

    val wordsP = pipeline.fit(words).transform(words).cache()
    val notWordsP = pipeline.fit(notWords).transform(notWords).cache()

    val wordsCoverage =
      WordEmbeddingsModel.withCoverageColumn(wordsP, "embeddings", "cov_embeddings")
    val notWordsCoverage =
      WordEmbeddingsModel.withCoverageColumn(notWordsP, "embeddings", "cov_embeddings")

    wordsCoverage.select("word", "cov_embeddings").show(1)
    notWordsCoverage.select("word", "cov_embeddings").show(1)

    val wordsOverallCoverage =
      WordEmbeddingsModel.overallCoverage(wordsCoverage, "embeddings").percentage
    val notWordsOverallCoverage =
      WordEmbeddingsModel.overallCoverage(notWordsCoverage, "embeddings").percentage

    ResourceHelper.spark
      .createDataFrame(
        Seq(("Words", wordsOverallCoverage), ("Not Words", notWordsOverallCoverage)))
      .toDF("Dataset", "OverallCoverage")
      .show(1)

    assert(wordsOverallCoverage == 1)
    assert(notWordsOverallCoverage == 0)
  }

  "Word Embeddings" should "store and load from disk" taggedAs FastTest in {

    val data =
      ResourceHelper.spark.read
        .option("header", "true")
        .csv("src/test/resources/embeddings/clinical_words.txt")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("word")
      .setOutputCol("document")

    val tokenizer = new Tokenizer()
      .setInputCols(Array("document"))
      .setOutputCol("token")

    val embeddings = new WordEmbeddings()
      .setStoragePath("src/test/resources/random_embeddings_dim4.txt", ReadAs.TEXT)
      .setDimension(4)
      .setStorageRef("glove_4d")
      .setInputCols("document", "token")
      .setOutputCol("embeddings")

    val pipeline = new Pipeline()
      .setStages(Array(documentAssembler, tokenizer, embeddings))

    val model = pipeline.fit(data)

    model.write.overwrite().save("./tmp_embeddings_pipeline")

    model.transform(data).show(1)

    val loadedPipeline1 = PipelineModel.load("./tmp_embeddings_pipeline")

    loadedPipeline1.transform(data).show(1)

    val loadedPipeline2 = PipelineModel.load("./tmp_embeddings_pipeline")

    loadedPipeline2.transform(data).show(1)
  }

}
