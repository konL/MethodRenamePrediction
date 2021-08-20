#! -*- coding: utf-8 -*-

import tensorflow as tf


config = tf.compat.v1.ConfigProto()
config.gpu_options.allow_growth = True
session = tf.compat.v1.InteractiveSession(config=config)

import json
import pandas as pd
import numpy as np
import tensorflow as tf
import keras
import os

# 指定第一块GPU可用

os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"

os.environ["CUDA_VISIBLE_DEVICES"] = "1"



from bert4keras.tokenizers import Tokenizer
from bert4keras.snippets import sequence_padding, DataGenerator
from sklearn.metrics import classification_report, f1_score
from bert4keras.optimizers import Adam

from keras4Bert_textCNN import build_bert_model
from keras4bert_dataset import load_data

#定义超参数和配置文件
class_nums = 2
#13
maxlen = 320
#128
batch_size = 8

config_path='C:\\Users\\delll\\Desktop\\liangjh\\iden_project\\uncased_L-12_H-768_A-12\\bert_config.json'
checkpoint_path='C:\\Users\\delll\\Desktop\\liangjh\\iden_project\\uncased_L-12_H-768_A-12\\bert_model.ckpt'

dict_path='C:\\Users\\delll\\Desktop\\liangjh\\iden_project\\uncased_L-12_H-768_A-12\\vocab.txt'

tokenizer = Tokenizer(dict_path)

class data_generator(DataGenerator):

    """
    数据生成器
    """
    def __iter__(self, random=False):
        batch_token_ids, batch_segment_ids, batch_labels = [], [], []
        token_len=0
        index=1
        # for is_end, (text,text1, label) in self.sample(random):
        for is_end, (oldname,text,text1, edge,label) in self.sample(random):

            # # print("text=", text)
            # # print("text1=", text1)
            # index1 = text.find("{")
            #
            # index2 = text1.find("{")
            # if (index1==-1 or index2==-1):
            #     text = text
            #     text1 = text1
            # else:
            #     text = text[index1:]
            #     text1 = text1[index2 :]
            # # print("text=",text)
            # # print("text1=",text1)


            token_ids, segment_ids = tokenizer.encode(text,text1, maxlen=maxlen)
            # print(text1)
            # print(text)
            # print("token len=",len(token_ids))
            # token_len=token_len+(len(token_ids)-2)
            # print(index,"]sum len=", token_len)
            # index=index+1
            # print("text1=",text)
            # print("text2=", text1)
            # print("token ids=", token_ids)
            # print("segment_ids=", segment_ids)

            batch_token_ids.append(token_ids)
            batch_segment_ids.append(segment_ids)
            batch_labels.append([label])
            if len(batch_token_ids) == self.batch_size or is_end:
                batch_token_ids = sequence_padding(batch_token_ids)
                batch_segment_ids = sequence_padding(batch_segment_ids)
                batch_labels = sequence_padding(batch_labels)
                yield [batch_token_ids, batch_segment_ids], batch_labels
                batch_token_ids, batch_segment_ids, batch_labels = [], [], []
from tensorflow.keras import backend as K
def recall(y_true,y_pred):

    true_positive = K.sum(K.round(K.clip(y_true*y_pred, 0, 1)))
    possible_positive = K.sum(K.round(K.clip(y_true, 0, 1)))
    recall=true_positive/(possible_positive+K.epsilon())
    return recall
def precision(y_true,y_pred):
    true_positive = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    predicted_positive = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision=true_positive/(predicted_positive+K.epsilon())
    return precision
def f1(y_true,y_pred):
    def recall(y_true,y_pred):

        true_positive = K.sum(K.round(K.clip(y_true*y_pred, 0, 1)))
        possible_positive = K.sum(K.round(K.clip(y_true, 0, 1)))
        recall=true_positive/(possible_positive+K.epsilon())
        return recall
    def precision(y_true,y_pred):
        true_positive = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
        predicted_positive = K.sum(K.round(K.clip(y_pred, 0, 1)))
        precision=true_positive/(predicted_positive+K.epsilon())
        return precision
    precision=precision(y_true,y_pred)
    recall=recall(y_true,y_pred)
    return 2*((precision*recall)/(precision+recall+K.epsilon()))
from sklearn.metrics import roc_auc_score


if __name__ == '__main__':

    proj="camel"
    issmall=""
    # 加载数据集
    train_data ,train= load_data('C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\train_data_6x\\3x\\'+proj+'_train.csv' )
    val_data ,val= load_data('C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\test_data_6x\\no_order\\beam_test_mask_change.csv')

    # train_data ,train= load_data('beam_zeppelin_prepocessed.csv')
    # val_data ,val= load_data('bi_train_method.csv')

    # test_data, test = load_data(
    #     'C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\test_data_6x\\beam_prepocessed_02.csv')
    test_data, test = load_data('C:\\project\\IdentifierStyle\\data\\VersionDB\\prepocessed_data\\test_data_6x\\no_order\\'+proj+'_test_mask_change'+issmall+'.csv')

    # # test_data, test = load_data('bi_train_method.csv')
    print(train['label_class'].value_counts())


    columns = train.columns


    # 删除最后一列，即class列
    features_columns = columns.delete(len(columns) - 1)


    # 获取除class列以外的所有特征列
    features = train[features_columns]

    # 获取class列
    labels = train['label_class']

    # #划分原始数据训练集和测试集用于oversample模型生成

    # RandomUnderSampler函数是一种快速并十分简单的方式来平衡各个类别的数据: 随机选取数据的子集.
    from imblearn.under_sampling import RandomUnderSampler

    rus = RandomUnderSampler(random_state=0)
    os_features, os_labels = rus.fit_resample(features, labels)
    # 新生成的数据集
    train = pd.concat([os_features, os_labels], axis=1)
    print(train['label_class'].value_counts())
    train_data=train.values


    # # # # #
    # columns = test.columns
    #
    # # 删除最后一列，即class列
    # features_columns = columns.delete(len(columns) - 1)
    #
    # # 获取除class列以外的所有特征列
    # test_features = test[features_columns]
    #
    # # 获取class列
    # test_labels = test['label_class']
    #
    # # #划分原始数据训练集和测试集用于oversample模型生成
    #
    # os_features1, os_labels1 = rus.fit_resample(test_features, test_labels)
    # # 新生成的数据集
    # test = pd.concat([os_features1, os_labels1], axis=1)
    # print(test['label_class'].value_counts())
    # test_data = test.values

    # 转换数据集
    train_generator = data_generator(train_data, batch_size)
    val_generator = data_generator(val_data, batch_size)
    test_generator = data_generator(test_data, batch_size)
    import keras_metrics as km
    model = build_bert_model(config_path,checkpoint_path,class_nums)
    model.compile(
        loss='sparse_categorical_crossentropy',
        optimizer=Adam(5e-6),
        metrics=['accuracy',f1,precision,recall],
    )

    earlystop = keras.callbacks.EarlyStopping(
        monitor='val_loss',
        patience=2,
        verbose=1,
        mode='min'
        )
    bast_model_filepath = 'v3_best_model.weights'
    checkpoint = keras.callbacks.ModelCheckpoint(
        bast_model_filepath,
        monitor='val_loss',
        verbose=2,
        save_best_only=True,
        mode='min'
        )
    from sklearn.utils import class_weight
    import numpy as np


    history=model.fit_generator(

        train_generator.forfit(),
        steps_per_epoch=len(val_generator),
        epochs=20,

        validation_data=val_generator.forfit(),
        validation_steps=len(val_generator),
        shuffle=True,
        verbose=1,

        callbacks=[earlystop,checkpoint]
    )

    # history_dict = history.history
    # print(history_dict.keys())
    #
    # # 训练loss
    # # 绘制训练损失，每轮都下降
    # import matplotlib.pyplot as plt
    #
    # loss_values = history_dict['loss']
    # val_loss_values = history_dict['val_loss']
    # epochs = range(1, len(loss_values) + 1)
    # plt.plot(epochs, loss_values, 'r', label='Training loss')
    # plt.plot(epochs, val_loss_values, 'b', label='Validation loss')
    # plt.title('Training and validation loss')
    # plt.xlabel('Epochs')
    # plt.ylabel('Loss')
    # plt.legend()
    # plt.show()
    #
    # plt.clf()
    # f1 = history_dict['f1']
    # val_f1 = history_dict['val_f1']
    # plt.plot(epochs, f1, 'r', label='Training f1_score')
    # plt.plot(epochs, val_f1, 'b', label='Validation f1_score')
    # plt.title('Training and validation f1_score')
    # plt.xlabel('Epochs')
    # plt.ylabel('f1_score')
    # plt.legend()
    # plt.show()
    # plt.clf()
    # f1 = history_dict['precision']
    # val_f1 = history_dict['val_precision']
    # plt.plot(epochs, f1, 'r', label='Training precision')
    # plt.plot(epochs, val_f1, 'b', label='Validation precision')
    # plt.title('Training and validation precision')
    # plt.xlabel('Epochs')
    # plt.ylabel('precision')
    # plt.legend()
    # plt.show()
    # plt.clf()
    # f1 = history_dict['recall']
    # val_f1 = history_dict['val_recall']
    # plt.plot(epochs, f1, 'r', label='Training recall')
    # plt.plot(epochs, val_f1, 'b', label='Validation recall')
    # plt.title('Training and validation recall')
    # plt.xlabel('Epochs')
    # plt.ylabel('recall')
    # plt.legend()
    # plt.show()
    # score,  f1, precision, recall = model.evaluate(test_generator, steps=50,
    #                                                           max_queue_size=10,
    #                                                           use_multiprocessing=False)
    # print('score:', score, 'f1:', f1, 'precision:', precision, 'recall', recall)
    model.load_weights('v3_best_model.weights')
    test_pred = []
    test_true = []
    #+++++++++++++++++++++







    # for x,y in test_generator:
    #
    #     p = model.predict(x).argmax(axis=1)
    #     test_pred.extend(p)
    #
    # test_true = test_data[:,2].tolist()
    for x, y in test_generator:
        p = model.predict(x).argmax(axis=1)
        test_pred.extend(p)

    test_true = test_data[:, 4].tolist()

    print("项目=",proj)
    fp = 0
    tp = 0
    fn = 0
    tn = 0
    index_i = 0
    for i in range(len(test_true)):
        pred = int(test_pred[i])



            # index_i=index_i+1

        # pred = int(test_pred[i])

        if ((test_true[i] == pred) & (test_true[i] == 0)):
            tn = tn + 1
        if ((test_true[i] == pred) & (test_true[i] == 1)):
            tp = tp + 1
        if ((test_true[i] != pred) & (test_true[i] == 0)):
            fp = fp + 1
        if ((test_true[i] != pred) & (test_true[i] == 1)):
            # print(test_true[i], " ", test_pred[i])
            # print(test_true[i] == 1)
            # print(type(test_true[i] ))
            # print(type(pred))
            fn = fn + 1

    print(tp, fp, tn, fn)

    precision = tp / (tp + fp)
    recall = tp / (tp + fn)

    print("f1:", 2 * ((precision * recall) / (precision + recall)))
    print("precision:", precision)
    print("recall:", recall)
    # target_names = [line.strip() for line in open('label','r',encoding='utf8')]
    # print(classification_report(test_true, test_pred,target_names=target_names))
    print(classification_report(test_true, test_pred))



    # name_set = test['oldname'].tolist()
    # #
    # #
    # def calChangeNum(i,test_pred):
    #     data=test["edge"].tolist()
    #     x=data[i]
    #     #获取第i个标识符的相关实体集合 edge
    #     edges = x.split("|")
    #
    #     changeEnt = 0
    #
    #     for e in edges:
    #         index = e.find(',')
    #         # 获取相关实体
    #         node = e[index + 1:-1].strip()
    #         # 在test_pred中
    #
    #         if node in name_set:
    #             index = name_set.index(node)
    #             if test_pred[index] == 1:
    #                 changeEnt = changeEnt + 1
    #         # else:
    #         #     print(node, changeEnt)
    #         #     changeEnt = changeEnt + 0
    #
    #
    #     #     print(sumEnt)
    #     return changeEnt


    changeNum=test['changeNum'].tolist()
    print(len(changeNum),len(test_pred),len(test_true))
    fp=0
    tp=0
    fn=0
    tn=0
    index_i=0
    for i in range(len(test_true)):
        pred = int(test_pred[i])

        if((test_pred[i]==1)&(changeNum[i]==0)):
        #     pred=0
        # if ((test_pred[i] == 1) & (calChangeNum(i,test_pred) ==0 )):
            test_pred[i]=0
            pred=0


            # index_i=index_i+1


        # pred = int(test_pred[i])

        if((test_true[i]==pred) & (test_true[i]==0)):
            tn=tn+1
        if ((test_true[i] == pred) & (test_true[i]==1)):
            tp=tp+1
        if ((test_true[i] != pred) & (test_true[i] == 0)):
            fp=fp+1
        if ((test_true[i] != pred) & (test_true[i] == 1)):
            # print(test_true[i], " ", test_pred[i])
            # print(test_true[i] == 1)
            # print(type(test_true[i] ))
            # print(type(pred))
            fn=fn+1

    print(tp,fp,tn,fn)

    precision=tp/(tp+fp)
    recall=tp / (tp + fn)

    print("f1:", 2*((precision*recall)/(precision+recall)))
    print("precision:", precision)
    print("recall:", recall)




