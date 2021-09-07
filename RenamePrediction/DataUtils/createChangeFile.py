#做统计
import pandas as pd
proj="flink"

#为了防止出现 Error tokenizing data.，加上delimeter
data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_t.csv",delimiter=",",header=None,names =['label_class','type','oldname','newname','oldStmt', 'newStmt','edge'])
data_df

data_df[['oldname','newname']].head()
# 建立一个word_Index 单词：label
word_index = {}
changeFile = pd.read_csv("C:\\project\\IdentifierStyle\\log\\dump\\" + proj + ".csv", delimiter=",",header=None)
for indexs in changeFile.index:
    ent = changeFile.loc[indexs].values[3].split('<-')
    # 最后会有空的
    oneent = ent[len(ent) - 2]
    print(oneent)

    #     print(type(ent),type(str(label)))
    word_index.update({oneent.strip(): 1})

# changeFile
print(len(word_index))
print(len(changeFile))


def process(x):
    ent = x.split('<-')
    #最后一个，最旧的名字
    oneent = ent[len(ent) - 2]
    return oneent


nameSet = changeFile[3].apply(lambda x: process(x)).tolist()
print(len(nameSet))





def find_change(index, changeFile):
    change_relate_Ent = {}
    print(index)
    for i in range(index, len(changeFile)):
        ent = changeFile.loc[i].values[3].split('<-')
        oneent = ent[len(ent) - 2]
        change_relate_Ent.update({oneent.strip(): 1})
    return change_relate_Ent



change_relate_Ent = find_change(0, changeFile)
def cal_no_order(x):
    edges = x.split("|")
    node = set()
    changeEnt = 0
    sumEnt = len(edges)
    index = edges[0].find(',')
    name = edges[0][1:index].strip()
    for e in edges:
        index = e.find(',')
        #实体node
        node = e[index + 1:-1].strip()
        score = change_relate_Ent.get(node)
        if score != None:
            changeEnt = changeEnt + 1

    #     print(changeEnt)
    #     print(sumEnt)
    return changeEnt



data_df['changeNum'] = data_df["edge"].apply(lambda x: cal_no_order(x))

#在与某个标识符相关的实体集合中，含有的实体变化个数
print(data_df['changeNum'].value_counts())
print(data_df['label_class'].value_counts())
print(data_df.loc[data_df['changeNum'] ==0]['label_class'].value_counts())
print(data_df.loc[data_df['changeNum'] >0]['label_class'].value_counts())
#获取得到最后的test_data
data_df.to_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_change.csv")
