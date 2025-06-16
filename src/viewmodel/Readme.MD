rm -rf bin

javac -d bin -cp "lib/*" src/**/*.java

cp -r src/assets bin/assets

java -cp "bin:lib/*" main.Main