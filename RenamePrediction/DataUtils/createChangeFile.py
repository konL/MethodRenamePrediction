#做统计
import pandas as pd
proj="flink"

#为了防止出现 Error tokenizing data.，加上delimeter
data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_t.csv",delimiter=",",header=None,names =['label_class','type','oldname','newname','oldStmt', 'newStmt','edge'])
data_df
# data_df=pd.read_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\changeIdentifier\\zeppelin_result.csv",header=0)
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


# 这里要实现获得当前处理标识符的index，index之前根据edge找
# def cal(x):
#     edges=x.split("|")
#     node=set()
#     changeEnt=0
#     sumEnt=len(edges)
#     for e in edges:
#         index=e.find(',')
#         node=e[index+1:-1].strip()
#         score=word_index.get(node)
#         if score!=None:
#             print(node,changeEnt)
#             changeEnt=changeEnt+word_index.get(node)
#         else:
#             print(node,changeEnt)
#             changeEnt=changeEnt+0

# 实际整个相关都是oldname相关的实体


def find_change(index, changeFile):
    change_relate_Ent = {}
    print(index)
    for i in range(index, len(changeFile)):
        ent = changeFile.loc[i].values[3].split('<-')
        oneent = ent[len(ent) - 2]
        change_relate_Ent.update({oneent.strip(): 1})
    return change_relate_Ent


def cal(x):
    edges = x.split("|")
    node = set()
    changeEnt = 0
    sumEnt = len(edges)
    index = edges[0].find(',')
    name = edges[0][1:index].strip()
    if word_index.get(name) != None:
        # label=1
        #         cur_index=len(nameSet) - 1 - nameSet[::-1].index(name)
        cur_index = nameSet.index(name)
    #         print("label=1")

    else:
        cur_index = 0
    #         print("label=0")
    # 在proj.csv中，index后的相关实体修改是已知的

    change_relate_Ent = find_change(cur_index, changeFile)
    if cur_index == 0:
        print("label=0", len(change_relate_Ent))
    else:
        print("label=1", len(change_relate_Ent))
    for e in edges:
        index = e.find(',')
        node = e[index + 1:-1].strip()
        score = change_relate_Ent.get(node)
        if score != None:
            changeEnt = changeEnt + 1

    #     print(changeEnt)
    #     print(sumEnt)
    return changeEnt
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
#
# data_df['changeRate']=data_df["changeNum"].apply(lambda x: calRate(,x.indexs))
#读取order.txt
# order={}
# index_order=0
# f = open("C:\\project\\IdentifierStyle\\log\\dump\\"+proj+".txt","r" ,encoding='UTF-8')
# lines = f.readlines()      #读取全部内容 ，并以列表方式返回
# for line in lines:
#     # print(line)
#     data=line.split(" ")
#     line=data[0]
#     order.update({line:index_order})
#     index_order=index_order+1
# print(order)
# print(len(order))

# def calWithOrder(x):
#     edges = x.split("|")
#     changeEnt = 0
#     index = edges[0].find(',')
#     name = edges[0][1:index].strip()
#     #该标识符重命名了
#     if word_index.get(name) != None:
#         #获取在changeFile的index
#         cur_index = nameSet.index(name)
#         #获取commitId
#         cur_id=changeFile.loc[cur_index].values[4][:end]
#         changename=changeFile.loc[cur_index].values[3]
#         print("name="+name,"changename="+changename,"id=",cur_id)
#         order_index=order.get(cur_id.strip())
#         print(order_index)
#     else:
#         order_index = 0
#
#     # if cur_index == 0:
#     #     print("label=0", len(change_relate_Ent))
#     # else:
#     #     print("label=1", len(change_relate_Ent))
#     for e in edges:
#         index = e.find(',')
#         node = e[index + 1:-1].strip()
#         if order_index==0:
#             score = change_relate_Ent.get(node)
#             if score != None:
#                 changeEnt = changeEnt + 1
#         else:
#             if word_index.get(node) != None:
#                 res_index = nameSet.index(node)
#                 # 获取commitId
#                 res_id = changeFile.loc[res_index].values[4][:end]
#                 order_index_res = order.get(res_id)
#                 print(res_id)
#                 print(order_index_res)
#
#                 if( (order_index!=None) &(order_index_res!=None)):
#                     if(order_index<=order_index_res ):
#                         changeEnt=changeEnt+1
#                 else:
#                     changeEnt = changeEnt + 1
#
#     #     print(changeEnt)
#     #     print(sumEnt)
#     return changeEnt

# data_df['changeNum'] = data_df["edge"].apply(lambda x: cal(x))
data_df['changeNum'] = data_df["edge"].apply(lambda x: cal_no_order(x))

#在与某个标识符相关的实体集合中，含有的实体变化个数
print(data_df['changeNum'].value_counts())
print(data_df['label_class'].value_counts())
print(data_df.loc[data_df['changeNum'] ==0]['label_class'].value_counts())
print(data_df.loc[data_df['changeNum'] >0]['label_class'].value_counts())
data_df.to_csv("C:\\project\\IdentifierStyle\\data\\VersionDB\\raw_data\\test_data_6x\\"+proj+"_result_change.csv")