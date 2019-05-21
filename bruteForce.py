from math import sqrt
import random
import numpy as np
import sys
import time

class Point:
    def __init__(self,x_init,y_init, field_init):
        self.x = x_init
        self.y = y_init
        self.field_init = field_init

    def getX(self):
        return self.X

    def getY(self):
        return self.Y

    def __repr__(self):
        return "".join(["{(", str(self.x), ",", str(self.y), "), ", str(self.field_init),"}"])


def distance(a, b):
    return sqrt((a.x-b.x)**2+(a.y-b.y)**2)


def partition(collection):

    if len(collection) == 1:
        yield [collection]
        return

    first = collection[0]
    for smaller in partition(collection[1:]):
        # insert `first` in each of the subpartition's subsets
        for n, subset in enumerate(smaller):
            yield smaller[:n] + [[first] + subset]  + smaller[n+1:]
        # put `first` in its own subset 
        yield [[first]] + smaller



def main():

    # n = int(input("Enter the number of points to generate: "))

    # random_numbers = (random.sample(range(100), 2*n))
    # # print(random_numbers)
    # pointsForFile = []
    # counter = 0
    # numOfPointsToGenerate = n

    # for x in range(numOfPointsToGenerate):
    #     pointsForFile.append(str(random_numbers[counter]) + "," + str(random_numbers[counter+1]))
    #     counter = counter + 2

    # i = 0
    # with open("points2.txt", "w") as f1:
    #     for e in pointsForFile:
    #         if i == n-1:
    #             f1.write(str(e))
    #         else:
    #             f1.write(str(e) + "\n")
    #         i+= 1


    numOfPoints = 0
    points = []
    with open("points.txt", "r") as f:
        lines = f.read().split("\n")
        i = 0
        for line in lines:
            l = line.split(",")
            points.append(Point(int(l[0]), int(l[1]), i))
            i += 1
            numOfPoints += 1
            

    print(points)

    matrix = np.zeros((numOfPoints,numOfPoints))

    for i in range(numOfPoints):
        for j in range(numOfPoints):
            matrix[i][j] = distance(points[i], points[j])

    print(matrix)


    k = int(input("Unesite zeljeni broj klastera: \n"))

    startTime = time.time()

    disjointSets = list()
    
    for n, p in enumerate(partition(points), start=1):

        if len(p) != k:
            continue
        
        ind = 1
        for i in range(k):
            if len(p[i]) < 1:
                ind = 0
                break

        if  ind == 1:
            # print(n, p)
            disjointSets.append(p)


    listOfMax = list()

    for e in disjointSets:
        # print(e)
        listOfMaxDistInE = list()
        for s in e:
            maxDistInSet = 0
            distance1 = 0
            for i in range(len(s)):
                # print(s[i])
                for j in range(i, len(s)):
                    # distance1 = distance(s[i], s[j])
                    distance1 = matrix[s[i].field_init][s[j].field_init]
                    if distance1 > maxDistInSet:
                        maxDistInSet = distance1

            listOfMaxDistInE.append(maxDistInSet)

        # print(listOfMaxDistInE)
        # print()
        maxInE = max(listOfMaxDistInE)
        # print(maxInE)
        listOfMax.append(maxInE)

    endTime = time.time()

    print()
    print(min(listOfMax))
    print(str(endTime - startTime) + " seconds")


if __name__ == '__main__':
    main()