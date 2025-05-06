import recommendationflask
#Return the not noted movies which it is in the genre and most averaged-noted  

#Requete SQL SELECT Movies.* FROM Ratings Jointure Movies WHERE user.id=u AND rating = 0 AND genre=genre 
#TO DO
def Notrated(u=0, genre="Action"):
    return recommendationflask.unrated_movies(u,genre)


#Return the matrix genre/average note/count of user [genre1,average1,count1],[genre2,average2,count2] 

# Requete SQL : SELECT genre, avg(rating), count() FROM Ratings Jointure Movies WHERE user.id=u AND NOT(rating = 0) GROUP BY genre
#TO DO
def Matrixgenrerating(u=0): 
    return recommendationflask.ratings_by_genre(u)


CW=1 # count weight
RW=1 # rating weight 


u=0 # user 
M=Matrixgenrerating(u)
i=0 
j=0

    
G=[["Action",0],["Adventure",0],["Animation",0],["Children's",0],["Comedy",0],["Crime",0],["Documentary",0],["Crime",0]
,["Documentary",0],["Drama",0],["Fantasy",0],["Film-Noir",0],["Horror",0],["Musical",0],["Mystery",0],["Romance",0],["Sci-Fi",0]
,["Thriller",0],["War",0],["Western",0]]



while i < len(M):
    if (G[j][0]==M[i][0]):
        G[j][1]=M[i][1]*RW*M[i][2]*CW
        i+=1
    j+=1

maxg=0
maxi=G[0][1]
for j in range(1,len(G)):
    if G[j][1]>maxi:
        maxi=G[j][1]
        maxg=j

print(G)
print(G[maxg][0])
print(Notrated(u,G[maxg][0]))