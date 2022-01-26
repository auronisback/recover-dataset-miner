# recover-dataset-miner

Mining tool to recreate ReCover dataset.

## Usage

To produce an executable jar for the tool, execute:

```
mvn clean package
```

One the package is complete, locate the file _dataset-builder-1.0.0-SNAPSHOT.jar_ in the _target_ folder. You can start the mining tool by executing:

```
java -jar dataset-builder-1.0.0-SNAPSHOT.jar <path to IR dataset projectinfo.csv> <path to RTP torrent package folder>
```
