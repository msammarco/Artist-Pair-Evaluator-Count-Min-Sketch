# Artist Pair Evaluator - Count-Min Sketch

## About

The text file "Artist_lists_small.txt" contains the favorite musical artists of 1000 users from [LastFM](http://www.last.fm/). Each line is a list of up to 50 artists, formatted as follows:

```
Radiohead,Pulp,Morrissey,Delays,Stereophonics,Blur,Suede,Sleeper,The La's,Super Furry Animals\n
Band of Horses,Iggy Pop,The Velvet Underground,Radiohead,The Decemberists,Morrissey,Television\n
etc.
```

Write a program that, using this file as input, produces an output file containing a list of pairs of artists which appear TOGETHER in at least fifty different lists. For example, in the above sample, Radiohead and Morrissey appear together twice, but every other pair appears only once. 

Now, imagine this list is a continual stream of information, and that you cannot store a list of all possible pairs of bands due to space constraints. How do you handle this situation?


## Enter Count Min Sketch

We use the probabilistic algorithm Count Min Sketch to analyze our data! Count Min Sketch uses sub-linear space so that all pairs aren't stored, making it a great data structure to use on a large stream of data. It does this at the expense of accuracy by storing a lossy representation of the data. Count Min Sketch was originally developed by G. Cormode and S. Muthukrisnan. More information can be found here [Count Min Sketch](http://en.wikipedia.org/wiki/Count%E2%80%93min_sketch)

