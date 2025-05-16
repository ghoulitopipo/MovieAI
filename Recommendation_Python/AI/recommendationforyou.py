#import recommendationflask
import write
#Return the not noted movies which it is in the genre and most averaged-noted

#Requete SQL SELECT Movies.* FROM Ratings Jointure Movies WHERE user.id=u AND rating = 0 AND genre=genre
def Notratedonegenre(u=0, genre="Action",nbre=1):
    return []
    #return recommendationflask.unrated_movies(u,genre,nbre)




#Return the matrix genre/average note/count of user [genre1,average1,count1],[genre2,average2,count2] 

# Requete SQL : SELECT genre, avg(rating), count() FROM Ratings Jointure Movies WHERE user.id=u AND NOT(rating = 0) GROUP BY genre
def Matrixgenrerating(u=0):
    return [["Action",3,3],["Animation",1.9,15],["War",4.2,20],["Western",5,2]]
    #return recommendationflask.ratings_by_genre(u)




u=0 # user 

def Likedgenres(u=0):
    CW=1 # count weight
    RW=10 # rating weight 
    N=100 # quantity of recommended movies
    M=Matrixgenrerating(u)

    
    G=[["Action",0],["Adventure",0],["Animation",0],["Children's",0],["Comedy",0],["Crime",0],["Documentary",0],["Crime",0]
    ,["Documentary",0],["Drama",0],["Fantasy",0],["Film-Noir",0],["Horror",0],["Musical",0],["Mystery",0],["Romance",0],["Sci-Fi",0]
    ,["Thriller",0],["War",0],["Western",0]]


    s=0
    i=0
    j=0
    while i < len(M):
        if (G[j][0]==M[i][0]):
            G[j][1]=M[i][1]*RW+M[i][2]*CW
            s+=G[j][1]
            i+=1
        j+=1
    j=0
    while j < len(G):
        G[j][1]=(G[j][1]*N)//s
        j+=1
    G.sort(key=lambda x: x[1],reverse=True)
    return G


print(Likedgenres())
print(Notratedonegenre(u,G[maxg][0]))

