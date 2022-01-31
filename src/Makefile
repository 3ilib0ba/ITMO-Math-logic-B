all: createJar

run:
	java -jar Main.jar

createJar:
	javac *.java -d .
	jar cfe Main.jar Main *.class

clean:
	rm *.class *.jar
