
import pandas as pd
proj="hbase"

#读取raw_data
# data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result.csv",header=None,names =['label_class','type','oldname','newname','oldStmt', 'newStmt'])
# print(data_df.head())

data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_change.csv",header=0)
print(data_df.head())

#生成处理好后的data
#1.去掉空方法体
#2.去掉oldStmt=newStmt

#新增一列记录：把方法名字去掉
def delName(x):
    index=x.find("{")
    if index!=-1:
        x=x[index:]


    return x

data_df['oldStmt_body'] = data_df["oldStmt"].apply(lambda x: delName(x))
data_df['newStmt_body'] = data_df["newStmt"].apply(lambda x: delName(x))
print(data_df.head())
#
#查看原来的label分布
print("【origin method】 \n",data_df['label_class'].value_counts())

#查看 空方法体的label分布
print("【Empty method】 \n",data_df.loc[data_df['oldStmt_body']=="{}"]['label_class'].value_counts())
print("【Empty method】 \n",data_df.loc[data_df['newStmt_body']=="{}"]['label_class'].value_counts())
data_df.drop(data_df[data_df['oldStmt_body']=="{}"].index, inplace=True)
data_df.drop(data_df[data_df['newStmt_body']=="{}"].index, inplace=True)
#查看 相同stmt的label分布，一般全是label=0
print("【same stmt】 \n",data_df.loc[data_df['oldStmt'] == data_df['newStmt']]['label_class'].value_counts())
data_df.drop(data_df[data_df['oldStmt'] == data_df['newStmt']].index, inplace=True)
#
#
#
data_df.to_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\train_data_6x\\"+proj+'_prepocessed_change.csv')
print(len(data_df))

print("【after prepocessed】 \n",data_df['label_class'].value_counts())
