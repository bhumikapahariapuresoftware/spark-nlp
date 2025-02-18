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

package com.johnsnowlabs.nlp

import org.apache.spark.ml.param.Param

trait HasClassifierActivationProperties extends ParamsAndFeaturesWritable {

  /** Whether to enable caching DataFrames or RDDs during the training
    *
    * @group param
    */
  val activation: Param[String] = new Param(
    this,
    "activation",
    "Whether to calcuate logits via Softmax or Sigmoid. Default is Softmax")

  setDefault(activation, ActivationFunction.softmax)

  /** @group getParam */
  def getActivation: String = $(activation)

  /** @group setParam */
  def setActivation(value: String): this.type = {

    value match {
      case ActivationFunction.softmax =>
        set(this.activation, ActivationFunction.softmax)
      case ActivationFunction.sigmoid =>
        set(this.activation, ActivationFunction.sigmoid)
      case _ =>
        set(this.activation, ActivationFunction.softmax)
    }

  }
}

object ActivationFunction {

  val softmax = "softmax"
  val sigmoid = "sigmoid"

}
