# Data Description
We provide test data (test_data) and training data can be constructed by ourselves. The strategy adopted in our paper is to use test data from nine of the projects combined into training data to predict one of the remaining projects.
The training set should be used to build your machine learning model. For the test set we provided, it was constructed several times. oldName and newName were used to construct label_class, edge was used to calculate changeNum, and oldStmt_body and newStmt_body were used to filter the data for the input model.
After the filtering is completed, oldStmt and newStmt are used as model inputs and label_class is the expected output.


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
