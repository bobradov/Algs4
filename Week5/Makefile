#JFLAGS = -g
JC = /usr/local/bin/javac-algs4
FB = /usr/local/bin/findbugs-algs4
CS = /usr/local/bin/checkstyle-algs4

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		PointSET.java \
		PointSETVisualizer.java \
		KdTree.java \
		KdTreeVisualizer.java \
		KdTester.java \
		Grd.java
	
        
default: classes



classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

findbugs:
	$(FB) *.class

style:
	$(CS) $(CLASSES)