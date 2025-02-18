---
layout: model
title: Pipeline to Detect Radiology Related Entities
author: John Snow Labs
name: ner_radiology_pipeline
date: 2022-03-21
tags: [licensed, ner, clinical, en]
task: Named Entity Recognition
language: en
edition: Spark NLP for Healthcare 3.4.1
spark_version: 3.0
supported: true
article_header:
  type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

This pretrained pipeline is built on the top of [ner_radiology](https://nlp.johnsnowlabs.com/2021/03/31/ner_radiology_en.html) model.

{:.btn-box}
<button class="button button-orange" disabled>Live Demo</button>
<button class="button button-orange" disabled>Open in Colab</button>
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/ner_radiology_pipeline_en_3.4.1_3.0_1647874212591.zip){:.button.button-orange.button-orange-trans.arr.button-icon}

## How to use



<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}
```python
pipeline = PretrainedPipeline("ner_radiology_pipeline", "en", "clinical/models")

pipeline.annotate("Breast ultrasound was subsequently performed, which demonstrated an ovoid mass measuring approximately 0.5 x 0.5 x 0.4 cm in diameter located within the left shoulder. This mass demonstrates isoechoic echotexture to the adjacent muscle, with no evidence of internal color flow. This may represent benign fibrous tissue or a lipoma.")
```
```scala
val pipeline = new PretrainedPipeline("ner_radiology_pipeline", "en", "clinical/models")

pipeline.annotate("Breast ultrasound was subsequently performed, which demonstrated an ovoid mass measuring approximately 0.5 x 0.5 x 0.4 cm in diameter located within the left shoulder. This mass demonstrates isoechoic echotexture to the adjacent muscle, with no evidence of internal color flow. This may represent benign fibrous tissue or a lipoma.")
```
</div>

## Results

```bash
+---------------------+-------------------------+
|chunk                |ner_label                |
+---------------------+-------------------------+
|Breast               |BodyPart                 |
|ultrasound           |ImagingTest              |
|ovoid mass           |ImagingFindings          |
|0.5 x 0.5 x 0.4      |Measurements             |
|cm                   |Units                    |
|left shoulder        |BodyPart                 |
|mass                 |ImagingFindings          |
|isoechoic echotexture|ImagingFindings          |
|muscle               |BodyPart                 |
|internal color flow  |ImagingFindings          |
|benign fibrous tissue|ImagingFindings          |
|lipoma               |Disease_Syndrome_Disorder|
+---------------------+-------------------------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|ner_radiology_pipeline|
|Type:|pipeline|
|Compatibility:|Spark NLP for Healthcare 3.4.1+|
|License:|Licensed|
|Edition:|Official|
|Language:|en|
|Size:|1.7 GB|

## Included Models

- DocumentAssembler
- SentenceDetectorDLModel
- TokenizerModel
- WordEmbeddingsModel
- MedicalNerModel
- NerConverter
