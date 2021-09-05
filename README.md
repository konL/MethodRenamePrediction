# Data Description
We provide test data (test_data) and training data can be constructed by ourselves. The strategy adopted in our paper is to use test data from nine of the projects combined into training data to predict one of the remaining projects.
The training set should be used to build your machine learning model. For the test set we provided, it was constructed several times. `oldName` and `newName` were used to construct `label_class`, `edge` was used to calculate `changeNum`, and `oldStmt_body` and `newStmt_body` were used to filter the data for the input model.
After the filtering is completed, `oldStmt` and `newStmt` are used as model inputs and `label_class` is the expected output.


|Column|Variable|Definition|Type|Content|
 -:|:-:|:-: |:-:| :-
 |2|label_class|Whether the method name needs to be renamed|Integer|0=No,1=Yes|
 |4|oldname|the historical method name|Text|outDegrees|
 |5|newname|the current method name|Text|getDegrees|
 |6|oldStmt|the historical method(masked name)|Text|`public DataSet<Tuple2<K, Long>> _(){return vertices.join(edges).where(0).equalTo(0).map(new VertexKeyWithOne<K, EV, VV>()).groupBy(0).sum(1);}`|
 |7|newStmt|the current method(masked name)|Text|`public DataSet<Tuple2<K, Long>> _(){return outDegrees().union(inDegrees()).groupBy(0).sum(1);}` |
 |8|edge|the set of the current entity and its related entity pairs|Text|`<outDegrees,Graph>,<outDegrees,flink.graphs>`|
 |9|changeNum|number of renaming activities performed in the related entity|Text| 1|
 |10|oldStmt_body|the historical method body|Text|`{return vertices.join(edges).where(0).equalTo(0).map(new VertexKeyWithOne<K, EV, VV>()).groupBy(0).sum(1);}`|
 |11|newStmt_body|the current method body|Text|`{return outDegrees().union(inDegrees()).groupBy(0).sum(1);}`|
 
 
 
 
 
 


# Quick Start
(1) Prepare data
**Note that:Preparing data will take a long time, the output data is the test data we provided(test_data).**
1. Collect the renaming data of the project
https://github.com/konL/MethodRenamePrediction/blob/dfaada6d4065241de25f587c36239d2b81ebdf46/RenamePrediction_preprocess/src/main/java/Extractor/HistoryAnalysis.java
2. Filter method renaming data
https://github.com/konL/MethodRenamePrediction/blob/07fff20c2f3933273a23bf4ea4d10cec61029c3f/RenamePrediction_preprocess/src/main/java/create_proj_method.java
4.Generate the old and new versions of the database based on the renaming data in step 2
https://github.com/konL/MethodRenamePrediction/blob/07fff20c2f3933273a23bf4ea4d10cec61029c3f/RenamePrediction_preprocess/src/main/java/createVerDB.java
5.generate test data
- Collect old info and new info from the old and new versions of the database
https://github.com/konL/MethodRenamePrediction/blob/dfaada6d4065241de25f587c36239d2b81ebdf46/RenamePrediction_preprocess/src/main/java/createEmbedding/creatResultFile.java
- Collect related entities and calculate the number of `changeNum`
https://github.com/konL/MethodRenamePrediction/blob/main/RenamePrediction/DataUtils/createChangeFile.py
- Filter method
https://github.com/konL/MethodRenamePrediction/blob/dfaada6d4065241de25f587c36239d2b81ebdf46/RenamePrediction/DataUtils/delStmt_processing.py
- Mask method name
https://github.com/konL/MethodRenamePrediction/blob/dfaada6d4065241de25f587c36239d2b81ebdf46/RenamePrediction/DataUtils/delDeclare.py
5. Generate training data
https://github.com/konL/MethodRenamePrediction/blob/dfaada6d4065241de25f587c36239d2b81ebdf46/RenamePrediction/DataUtils/createTraindata.py

(2) Training and prediction

6. Training and prediction
The test data of a project and its corresponding training data are selected for prediction, and the Precision, Recall and F-measure of the project are finally output.
https://github.com/SerVal-DTF/debug-method-RenamePrediction/prediction/keras4bert_loaddata.py
