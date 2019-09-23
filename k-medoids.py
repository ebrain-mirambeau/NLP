#Author: Ebrain Mirambeau
#Purpose: k-medoids algorithm

import math

f = open('/Users/ebrain_mirambeau/Desktop/vectors')

feat_vect = {}
features = {}
vector_lines = {}
vector = f.readline()
j = 0
clusters = {}

#calculate cosine similarity between words
def similarity(list1, list2):
    a = list1
    #print list1
    b = list2
    num = 0
    den = 0.01
    for i in range(len(a)):
        if 2*i > len(a) - 1:
            break
        else:
            for j in range(len(b)):
                if 2*j > len(b) - 1:
                    break
                else:
                    if a[2*i] == b[2*j]:
                        num+=float(features[a[2*i]]) * float(features[b[2*j]])
                        den+=math.sqrt(pow(float(features[a[2*i]]), 2))* math.sqrt(pow(float(features[b[2*j]]), 2))
    #print num/den
    return num/den   
      
while vector:
    vec = vector.split()
    vector_lines[j] = vec
    feat_vect[vec[0]] = vec[1:]
    v = vec[1:]
    #print v
    for i in range(len(v)):
        if 2*i + 1 >= len(v):
            break
        else:
            features[v[2*i]] = v[2*i+1]
    j += 1
    vector = f.readline()

#selecting initial medoids

N = len(feat_vect)
print N

C = 34
init_medoids = []

for i in range(0, C):
    init_medoids.insert(i, i * (N/C))

#clusters

for j in range(len(init_medoids)):
    clusters[vector_lines[j][0]] = [vector_lines[j][0]]

#algorithm
#each vector will be compared with every cluster index and min value, cluster index pair wins!
#feat_vect

a = feat_vect.keys()    
b = clusters.keys()
compare = []
for k in range(len(a)):
    for l in range(len(b)):
        compare.insert(l, (similarity(feat_vect[clusters[b[l]][0]], feat_vect[a[k]]), (b[l], a[k])))

    y = max(compare)
    clusters[y[1][0]].insert(len(clusters[y[1][0]]), y[1][1])
    compare = []

#printing clusters

for k in range(len(b)):
    print ' '.join(clusters[b[k]])


