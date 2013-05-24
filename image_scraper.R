#!/usr/bin/Rscript

args <- commandArgs(trailingOnly=T)
if (length(args) != 2){
  cat("Usage: ./test_script keyword number\n")
  quit()
}

libraries <- c("methods", "bitops", "devtools", "png", "XML", "RCurl", "stringr")
cat("Loading",length(libraries),"required libraries\n") # load required libraries
invisible(lapply(libraries, library, character.only = T))

is.wholenumber <- function(x, tol = .Machine$double.eps^0.5)  abs(x - round(x)) < tol

picLimit <- as.numeric(args[2])
if (!is.wholenumber(picLimit) && picLimit<0){
  cat("Second argument must be a positive integer\n")
  quit()
}

mainDir <- ".final_project"
binDir <- "bin"
imgDir <- "images"

dir.create(file.path("~",mainDir,binDir,imgDir), showWarnings=F, recursive=T)
setwd(file.path("~",mainDir))

urlBase <- "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
target <- args[1]
url <- paste(urlBase,target,sep="")

if (!url.exists(url)){
  cat("Error constructing search URL with",target,"as keyword\n")
  quit()
}
  
txt <- unlist(str_split(getURL(url), "\""))
imgURLs <- txt[str_detect(txt,".jpg")==T]
imgURLs <- imgURLs[(str_sub(imgURLs,-4,-1) == ".jpg")==T] # lots and lots of filtering
imgURLs <- imgURLs[(str_sub(imgURLs,1,4) == "http")==T] # further filtering
fullLen <- length(imgURLs) # see comment below
imgURLs <- unique(imgURLs) # remove duplicates

# CAUTION: The following script will literally download and save some number 
# of .JPG images to your working directory. Use at your own risk.

imgCount <- 0
cat("Downloading images\n")

# download the number of requested pictures
while(imgCount < picLimit){
  vecCount <- 1
  # get as many pictures possible from the current set of URLs
  while(vecCount <= length(imgURLs) && imgCount < picLimit){
    tempName <- paste(target,imgCount+1,sep="")
    download.result <- try(
      download.file(imgURLs[vecCount],   # This reads JPGs directly from the URL
                    paste(binDir,"/",imgDir,"/",tempName,".bmp",sep=""),  # and stashes them in the
                    mode = "wb",                                       # working directory
                    method = "curl")              
    )
    # move through vector, but only update our img counter on successful download
    vecCount <- vecCount + 1
    if (download.result == 0) # on success download.result will be zero
      imgCount <- imgCount + 1
    else
      cat("\n")
  }
  # check if we have enough pictures
  if (imgCount >= picLimit)
    break
  
  # construct a new search URL and set up a new vector of image URLs
  url <- paste(urlBase,target,"&start=",fullLen,sep="") # get pics after current
  txt <- unlist(str_split(getURL(url), "\""))
  imgURLs <- txt[str_detect(txt,".jpg")==T]
  # procede with filtering
  imgURLs <- imgURLs[(str_sub(imgURLs,-4,-1) == ".jpg")==T] # lots o' of filtering
  imgURLs <- imgURLs[(str_sub(imgURLs,1,4) == "http")==T]
  fullLen <- length(imgURLs) # fullLen is used above when constructing a new URL
                              # without it we wouldn't always skip over duplicates
  imgURLs <- unique(imgURLs) # remove duplicate URLs
}

cat("Success!",imgCount,"images saved\n")
