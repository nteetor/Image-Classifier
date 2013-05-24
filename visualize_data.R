# A big thank you to Chris DeSante for helping with plot creation

setwd("~/Documents/Sixth Semester/Artificial\ Intelligence/final_project/NeuralNetworkCurrent/R\ Stuff")
library(RColorBrewer) #Might need to install this.
library(ggplot2)
theme_set(theme_bw()) 
library(gridExtra)
library(reshape)
library(plyr)
DATA <- read.csv("network_results.csv")
head(DATA)
DATA$Correct <- ifelse(DATA$Predicted == DATA$Actual, 1, 0)
summary(DATA)
by.image <- melt(DATA, id=c("NAME", "Type","Trained"), na.rm=TRUE)
head(by.image)
table(by.image$variable)
CAST <- cast(data = by.image, formula= NAME+ Type +Trained  ~ variable, mean, na.rm=T)
head(CAST)

Categories <- c(names(table(CAST$Trained)))
Categories
Correctly <-  c(mean(CAST$Correct[CAST$Trained=="Beach"]),
    mean(CAST$Correct[CAST$Trained=="Castle"]),
    mean(CAST$Correct[CAST$Trained=="Desert"]),
    mean(CAST$Correct[CAST$Trained=="Green"]),
    mean(CAST$Correct[CAST$Trained=="Mountain"]),
    mean(CAST$Correct[CAST$Trained=="Red"]),
    mean(CAST$Correct[CAST$Trained=="Sunset"]),
    mean(CAST$Correct[CAST$Trained=="Tree"]))

qplot(Categories, Correctly, xlab = "Trained Against",
      ylab = "Proportion of Cases\n Correctly Classified",
      geom = "linerange", ymin = 0, ymax = Correctly,
      ylim = c(0,1), size= I(5), colour = factor(Categories)
) + guides(colour = "none")

 Cat.Sort <- factor( Categories, levels =  Categories[order(-Correctly)])

qplot(Cat.Sort, Correctly, xlab = "Trained Against",
      ylab = "Proportion of Cases\n Correctly Classified",
      geom = "linerange", ymin = 0, ymax = Correctly,
      ylim = c(0,1), size= I(5), colour = factor(Categories)
) + guides(colour = "none")

#8 x 8 Heatmap? 1s or NAs on Diagonal
HM <- matrix(data = 0, nrow = 8, ncol = 8)
colnames(HM) <- Categories[order(-Correctly)]
rownames(HM) <- Categories[order(-Correctly)]
diag(HM) <- 1
HM
Categories[order(-Correctly)]

{
  HM[2,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Green"]))
  HM[3,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Tree"]))
  HM[4,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Mountain"]))
  HM[5,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Sunset"]))
  HM[6,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Beach"]))
  HM[7,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Castle"]))
  HM[8,1]<-c(mean(CAST$Correct[CAST$Trained=="Red" & CAST$Type=="Desert"]))
  
  HM[1,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Red"]))
  HM[3,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Tree"]))
  HM[4,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Mountain"]))
  HM[5,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Sunset"]))
  HM[6,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Beach"]))
  HM[7,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Castle"]))
  HM[8,2]<-c(mean(CAST$Correct[CAST$Trained=="Green" & CAST$Type=="Desert"]))
  
  HM[1,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Red"]))
  HM[2,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Green"]))
  HM[4,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Mountain"]))
  HM[5,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Sunset"]))
  HM[6,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Beach"]))
  HM[7,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Castle"]))
  HM[8,3]<-c(mean(CAST$Correct[CAST$Trained=="Tree" & CAST$Type=="Desert"]))
  
  HM[1,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Red"]))
  HM[2,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Green"]))
  HM[3,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Tree"]))
  HM[5,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Sunset"]))
  HM[6,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Beach"]))
  HM[7,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Castle"]))
  HM[8,4]<-c(mean(CAST$Correct[CAST$Trained=="Mountain" & CAST$Type=="Desert"]))
  
  HM[1,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Red"]))
  HM[2,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Green"]))
  HM[3,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Tree"]))
  HM[4,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Mountain"]))
  HM[6,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Beach"]))
  HM[7,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Castle"]))
  HM[8,5]<-c(mean(CAST$Correct[CAST$Trained=="Sunset" & CAST$Type=="Desert"]))
  
  HM[1,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Red"]))
  HM[2,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Green"]))
  HM[3,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Tree"]))
  HM[4,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Mountain"]))
  HM[5,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Sunset"]))
  HM[7,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Castle"]))
  HM[8,6]<-c(mean(CAST$Correct[CAST$Trained=="Beach" & CAST$Type=="Desert"]))
  
  HM[1,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Red"]))
  HM[2,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Green"]))
  HM[3,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Tree"]))
  HM[4,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Mountain"]))
  HM[5,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Sunset"]))
  HM[6,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Beach"]))
  HM[8,7]<-c(mean(CAST$Correct[CAST$Trained=="Castle" & CAST$Type=="Desert"]))
  
  HM[1,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Red"]))
  HM[2,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Green"]))
  HM[3,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Tree"]))
  HM[4,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Mountain"]))
  HM[5,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Sunset"]))
  HM[6,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Beach"]))
  HM[7,8]<-c(mean(CAST$Correct[CAST$Trained=="Desert" & CAST$Type=="Castle"]))
  
}

HM                                                                      

head(CAST)
CAST[CAST$Trained=="Green" & CAST$Type=="Desert",]
HM 
HM.melt <- melt(HM)
HM.melt
library(RColorBrewer)
library(scales)
myPalette <- colorRampPalette((brewer.pal(4, "YlGnBu")))

HM1 <- ggplot(HM.melt,
              aes(x = X2, y = X1, fill = value))
HM1 <- HM1 + geom_tile()
HM1 <- HM1 + scale_fill_gradientn(colours = myPalette(100))
HM1 <- HM1 + scale_x_discrete(expand = c(0, 0))
HM1 <- HM1 + scale_y_discrete(expand = c(0, 0))
HM1 <- HM1 + coord_equal()
HM1 <- HM1 + labs(x  = "\n Classified Against",
                  y  = "Image Type\n", fill = "Proportion Correctly Classified")

print(HM1) 
HM[1,1]


HM.2 <- HM
for(i in 1:8){
  for(j in 1:8){
    
    XX <- (HM[rownames(HM)==rownames(HM)[i], colnames(HM)==colnames(HM)[j]] +
             HM[rownames(HM)==rownames(HM)[j], colnames(HM)==colnames(HM)[i]])/2
    
    HM.2[i,j] <- HM[i,j] - XX
    HM.2[j,i] <- HM[j,i] - XX
    
    
  }
  
}
HM.2

Map2 <- melt(HM.2)

myPalette <- colorRampPalette(rev(brewer.pal(5, "YlGnBu")))

HMap2 <- ggplot(Map2,
              aes(x = X2, y = X1, fill = value))
HMap2 <- HMap2 + geom_tile()
HMap2 <- HMap2 + scale_fill_gradient2(low=("darkorange1"),
                                      mid=("white"),
                                      high=("navy"))
  #colours = myPalette(100))
HMap2 <- HMap2 + scale_x_discrete(expand = c(0, 0))
HMap2 <- HMap2 + scale_y_discrete(expand = c(0, 0))
HMap2 <- HMap2 + coord_equal()
HMap2 <- HMap2 + labs(x  = "\n Classified Against",
                  y  = "Image Type\n", fill = "Pair-wise \n Classification Advantage")

print(HMap2) 


