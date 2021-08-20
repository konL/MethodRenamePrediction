#把每k-1个文件合并为一个train data数据集
projs=["zeppelin","flink","beam","tomcat","hbase","jmeter","cassandra","storm","dubbo","camel"]
import pandas as pd
for i in range(len(projs)):
    cur=projs[i]
    train_list = []
    data = pd.DataFrame()
    train_list.extend(projs[:i])
    train_list.extend(projs[i+1:])
    #根据train_list 读取 proj_prepocessed 再写入
    print(len(data))
    for proj in train_list:
        data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\test_data_6x\\"+proj+"_test_mask_change.csv",header=0)
        # data_df=data_df[['label_class','oldname','newname','oldStmt','newStmt']]

        data = pd.concat([data, data_df],sort=False)
    print(len(data))
    print(data.head())
    data.to_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\train_data_6x\\9x\\"+cur+"_train.csv")







