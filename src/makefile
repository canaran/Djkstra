JC = javac

%.class: %.java
	$(JC) $<

CLASSES =\
	Graph.java \
	FibonacciHeap.java \
	Node.java \
	Djkstra.java 


all: $(CLASSES:.java=.class)

clean:
	$(RM)*.class

