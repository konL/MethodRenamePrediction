import pandas as pd
proj="tomcat"
# data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\train_data_6x\\"+proj+"_prepocessed.csv")
data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\train_data_6x\\"+proj+"_prepocessed_change.csv")
oldname=data_df['oldname'].tolist()
newname=data_df['newname'].tolist()
#多处理一个步骤：把方法名字去掉
def delName(x,index,isOld):
    # index=x.find("{")
    # if index!=-1:
    #     x=x[index:]
    # name=data_df.iloc[index]['oldname']
    if isOld:
        name=data_df.iloc[index]['oldname']
    else:
        name = data_df.iloc[index]['newname']
    x=x.replace(name,"_",1)
    print(x)




    return x

data_df['oldStmt'] = data_df["oldStmt"].apply(lambda x: delName(x,data_df.loc[data_df['oldStmt']==x].index[0],True))
data_df['newStmt'] = data_df["newStmt"].apply(lambda x: delName(x,data_df.loc[data_df['newStmt']==x].index[0],False))

print(data_df.head())

data_df.to_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\test_data_6x\\no_order\\"+proj+"_test_mask_change.csv")
