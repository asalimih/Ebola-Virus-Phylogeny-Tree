setwd(dirname(rstudioapi::getActiveDocumentContext()$path))
library("seqinr")
library("Biostrings")
library("phytools")
library("phangorn")
library("ape")

ebola_names <- c('Zaire', 'TaiForest', 'Sudan', 'Reston', 'Bundibugyo')
gene_names <- c('NP', 'VP35', 'VP40', 'GP', 'VP30', 'VP24', 'L')

#------------------------------------reading data
genes_r <- list();#real genes
genes <- list();#founded genes
genomes <- list();# all genomes
genomes[['Marburg']] <- readDNAStringSet("Marburg_genome.fasta")
for(name in ebola_names) {
  genes_r[[name]] <- read.fasta(file = paste("RealGenes/",name,"_Genes.fasta",sep = ""))
  genes[[name]] <- read.fasta(file = paste("Founded_Genes/",name,"_Genes.fasta",sep = ""))
  genomes[[name]] <- readDNAStringSet(paste("Genomes/",name,"_genome.fasta",sep = ""))
  }
mar_genome = read.fasta(file = "Marburg_genome.fasta")

#reading distance matrices that was created in java code
dMat <- list()
dMat_r <- list()
for(name in gene_names) {
  dMat[[name]] <- matrix(scan(paste("CSVs/",name,".csv",sep = ""), sep=","),ncol=5)
  dMat_r[[name]] <- matrix(scan(paste("RealCSVs/",name,".csv",sep = ""), sep=","),ncol=5)
}
#----------------------------------------

#---------------creating Phylogeny Trees of each gene
upgma_trees <- list()
nj_trees <- list()
for(name in gene_names) {
  upgma_trees[[name]] <- upgma(dMat[[name]])
  upgma_trees[[name]][["tip.label"]] <- ebola_names
  nj_trees[[name]] <- nj(dMat[[name]])
  nj_trees[[name]][["tip.label"]] <- ebola_names
}

#----------------plotting the pheylogeny Trees of each gene
for(name in gene_names) {
  png(filename = paste("GeneTrees/UPGMA_",name,".png",sep = ""))
  plot(upgma_trees[[name]],use.edge.length = FALSE,main=paste("UPGMA - (",name,") Gene of Ebola"))
  edgelabels(round(upgma_trees[[name]]$edge.length), col="black", font=2)
  dev.off()
}

for(name in gene_names) {
  png(filename = paste("GeneTrees/NJ_",name,".png",sep = ""))
  plot(nj_trees[[name]],use.edge.length = FALSE,main=paste("Neighbor Joining - (",name,") Gene of Ebola"))
  edgelabels(round(nj_trees[[name]]$edge.length), col="black", font=2)
  dev.off()
}

#---------------creating combined phelogeny trees
upgma_tree_full <- list()
upgma_tree_full[['mean']] <- consensus.edges(upgma_trees)#mean
upgma_tree_full[['ls']] <- consensus.edges(upgma_trees,method="least.squares")#least squared

nj_tree_full <- list()
nj_tree_full[['mean']] <- consensus.edges(nj_trees)#mean
nj_tree_full[['ls']] <- consensus.edges(nj_trees,method="least.squares")#least squared

tree_full <- list()
tree_full[['mean']] <- consensus.edges(c(upgma_trees,nj_trees))#mean
tree_full[['ls']] <- consensus.edges(c(upgma_trees,nj_trees),method="least.squares")#least squared
#---------------plotting combined phelogeny trees
for (meth in c('mean','ls')) {
  png(filename = paste("CombinedTrees/UPGMA_",meth,".png",sep = ""))
  plot(upgma_tree_full[[meth]],use.edge.length = TRUE,main=paste("Combined UPGMA Trees - Method:",meth))
  edgelabels(round(upgma_tree_full[[meth]]$edge.length), col="black", font=2)
  dev.off()
  png(filename = paste("CombinedTrees/NJ_",meth,".png",sep = ""))
  plot(nj_tree_full[[meth]],use.edge.length = TRUE,main=paste("Combined NJ Trees - Method:",meth))
  edgelabels(round(nj_tree_full[[meth]]$edge.length), col="black", font=2)
  dev.off()
  png(filename = paste("CombinedTrees/ALL_",meth,".png",sep = ""))
  plot(tree_full[[meth]],use.edge.length = TRUE,main=paste("All Trees Combined - Method:",meth))
  edgelabels(round(tree_full[[meth]]$edge.length), col="black", font=2)
  dev.off()
}

#-----------------------------------creating trees based on global alignment 
subMat <- nucleotideSubstitutionMatrix(match = 0, mismatch = -1, baseOnly = TRUE)

genomes_aligned <- array(list(),c(6,6))
dMat_glob = matrix(nrow = 6,ncol = 6)

#global alignment between genomes and calculating levenshtein distance

for (i in 1:5) {
  for (j in (i+1):6) {
    genomes_aligned[i,j][[1]] <- pairwiseAlignment(paste(genomes[[i]],collapse = ""),
                                                   paste(genomes[[j]],collapse = ""), type="global",
                                                   substitutionMatrix = subMat,
                                                   gapOpening = 0, gapExtension = 1)
    genomes_aligned[j,i][[1]] <- genomes_aligned[i,j][[1]]
    dMat_glob[i,j] <-stringDist(c(paste(genomes[[i]],collapse = ""),
                                  paste(genomes[[j]],collapse = "")), method = "levenshtein")
    dMat_glob[j,i] = dMat_glob[i,j]
  }
}
#dMat_glob <- dMat_glob_withmar
upgma_tree_glob <- list()
upgma_tree_glob <- upgma(dMat_glob[-1,-1])
upgma_tree_glob[["tip.label"]] <- ebola_names
nj_tree_glob <- list()
nj_tree_glob <- nj(dMat_glob[-1,-1])
nj_tree_glob[["tip.label"]] <- ebola_names

png(filename = paste("CombinedTrees/UPGMA_Glob.png",sep = ""))
plot(upgma_tree_glob,use.edge.length = TRUE,main="Global Alignment + UPGMA")
edgelabels(round(upgma_tree_glob$edge.length), col="black", font=2)
dev.off()
png(filename = paste("CombinedTrees/NJ_Glob.png",sep = ""))
plot(nj_tree_glob,use.edge.length = TRUE,main="Global Alignment + NJ")
edgelabels(round(nj_tree_glob$edge.length), col="black", font=2)
dev.off()

#creating trees with upgma and nj
upgma_tree_glob_withmar <- list()
upgma_tree_glob_withmar <- upgma(dMat_glob)
upgma_tree_glob_withmar[["tip.label"]] <- c("Marburg",ebola_names)
nj_tree_glob_withmar <- list()
nj_tree_glob_withmar <- nj(dMat_glob)
nj_tree_glob_withmar[["tip.label"]] <- c("Marburg",ebola_names)

#plotting the trees 
png(filename = "CombinedTrees/UPGMA_Glob_withmar.png")
plot(upgma_tree_glob_withmar,use.edge.length = FALSE,main="Global Alignment + UPGMA + Marburg")
edgelabels(round(upgma_tree_glob_withmar$edge.length), col="black", font=2)
dev.off()
png(filename = "CombinedTrees/NJ_Glob_withmar.png")
plot(nj_tree_glob_withmar,use.edge.length = FALSE,main="Global Alignment + NJ + Marburg")
edgelabels(round(nj_tree_glob_withmar$edge.length), col="black", font=2)
dev.off()

#----------------------------Estimating times
#putting levinshtein distances in a matrix
p_mat <- matrix(nrow = 6, ncol = 6)
for (i in 1:6) {
  p_mat[i,i]<-1
}
#calculating simi
for (i in 1:5) {
  for (j in (i+1):6) {
    #calculating proportion of mismatches
    p_mat[i,j] <- nmismatch(genomes_aligned[i,j][[1]])/
      (nmatch(genomes_aligned[i,j][[1]])+nmismatch(genomes_aligned[i,j][[1]]))
    p_mat[j,i] = p_mat[i,j]
  }
}
time_mat <- matrix(nrow = 6, ncol = 6)
time_mat <- apply(p_mat,2,function (x) -0.75*(log(1-4*x/3)))#calculating Juke Cantor
time_mat <- round(time_mat/0.0019)#time distance between species
time_mat










