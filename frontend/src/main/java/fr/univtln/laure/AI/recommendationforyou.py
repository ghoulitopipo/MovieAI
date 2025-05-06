
#Return the not seen movie which it is in the genre and most averaged-noted 
#TO DO
def Notseen(u=0, genre="Action"):
    return movie


#Return the matrix genre/average note/count of user [genre1,average1,count1],[genre2,average2,count2] 
#TO DO
def Matrixrating(u=0): 
    return M


CW=1 # weight count
RW=1 # weight rating


u=0
M=Matrixrating(u)
i=0 
j=0

    
G=[["Action",0],["Adventure",0],["Animation",0],["Children's",0],["Comedy",0],["Crime",0],["Documentary",0],["Crime",0]
,["Documentary",0],["Drama",0],["Fantasy",0],["Film-Noir",0],["Horror",0],["Musical",0],["Mystery",0],["Romance",0],["Sci-Fi",0]
,["Thriller",0],["War",0],["Western",0]]



while i < len(M):
    if (G[j][0]==M[i][0]):
        G[j][1]=M[i][1]*RW+M[i][2]*CW
        i+=1
    j+=1

maxg=0
maxi=G[0][1]
for j in range(1,len(G)):
    if G[j][1]>maxi:
        maxi=G[j][1]
        maxg=g

print(Notseen(u,G[maxg][0]))