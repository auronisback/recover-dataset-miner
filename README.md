# recover-dataset-miner

Mining tool to recreate ReCover dataset.


## Usage

### Configuration
Create a copy of the _application.sample.yml_ in _main/resources_ folder.
The configuration parameters are:

- spring.security.oauth2.client.registration.github.clientId: User id to connect to GitHub
- spring.security.oauth2.client.registration.github.clientSecret: Secret token for the client
- github.user: username of github for password authentication
- github.pwd: the said password
- appconfig.clonebasepath: base path in which save the repositories
- appconfig.cloverdbdir: relative path to clover database
- appconfig.cloverMergedbdir: link to the clover merged database
- appconfig.surefirereportdir: folder where are stored surefire reports
- appconfig.timeoutMins: timeout to interrupt the mining of one job
- appconfig.requireAllTestOnPrev: if true, discards the pairs in which the previous version has some failing tests
- appconfig.pluginstoremove: a list of plugin to remove which can be incompatible with OpenClover
- asyncconfig.corePoolSize: number of starting thread pool size
- asyncconfig.maxPoolSize: maximum number of thread in the pool
- asyncconfig.queueCapacity: Maximum number of jobs in queue
- travisKey: token to access to TravisCI APIs

Set the configuration parameters accordingly to your needs.

### Execution 
To produce an executable jar for the tool, execute:

```
mvn clean package
```

One the package is complete, locate the file _dataset-builder-1.0.0-SNAPSHOT.jar_ in the _target_ folder. You can start the mining tool by executing:

```
java -jar dataset-builder-1.0.0-SNAPSHOT.jar <path to IR dataset projectinfo.csv> <path to RTP torrent package folder>
```

The IR dataset can be found here: https://sites.google.com/view/ir-based-tcp/resources and RTP Torrent is here: https://zenodo.org/record/3610999#.YfK7VerMJPY
