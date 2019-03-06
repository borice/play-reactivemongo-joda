# play-reactivemongo-joda
Demo project showing the JODA serialization issues with Play-ReactiveMongo

# Problem description
When using `play-json-joda` version `2.7.1` with Play 2.7 and `play2-reactivemongo` version `0.16.2-play27`, 
there is a problem with being able to write JODA `DateTime` structures to the database. The problem seems to 
exist in the serialization mechanism with ReactiveMogo (although I could be wrong about this).

_What's interesting is that the problem is only exhibited on `write` operations, not on `read`._

The exception trace is as follows:
```
[error] a.a.ActorSystemImpl - Uncaught error from thread [application-akka.actor.default-dispatcher-3]: scala/collection/compat/Factory$, shutting down JVM since 'akka.jvm-exit-on-fatal-error' is enabled for ActorSystem[application]
java.lang.NoClassDefFoundError: scala/collection/compat/Factory$
	at reactivemongo.play.json.JSONSerializationPack$Decoder$.children(JSONSerializationPack.scala:243)
	at reactivemongo.play.json.JSONSerializationPack$Decoder$.children(JSONSerializationPack.scala:219)
	at reactivemongo.api.commands.UpdateCommand$.$anonfun$reader$1(UpdateCommand.scala:119)
	at reactivemongo.api.commands.CommandCodecs$.$anonfun$dealingWithGenericCommandErrorsReader$1(CommandCodecs.scala:19)
	at reactivemongo.play.json.JSONSerializationPack$.$anonfun$reader$1(JSONSerializationPack.scala:157)
	at play.api.libs.json.Reads$$anon$6.reads(Reads.scala:195)
	at reactivemongo.play.json.JSONSerializationPack$.deserialize(JSONSerializationPack.scala:59)
	at reactivemongo.play.json.JSONSerializationPack$.deserialize(JSONSerializationPack.scala:33)
	at reactivemongo.api.SerializationPack.readAndDeserialize(SerializationPack.scala:36)
	at reactivemongo.api.SerializationPack.readAndDeserialize$(SerializationPack.scala:35)
Caused by: java.lang.ClassNotFoundException: scala.collection.compat.Factory$
	at java.net.URLClassLoader.findClass(URLClassLoader.java:382)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at reactivemongo.play.json.JSONSerializationPack$Decoder$.children(JSONSerializationPack.scala:243)
	at reactivemongo.play.json.JSONSerializationPack$Decoder$.children(JSONSerializationPack.scala:219)
	at reactivemongo.api.commands.UpdateCommand$.$anonfun$reader$1(UpdateCommand.scala:119)
	at reactivemongo.api.commands.CommandCodecs$.$anonfun$dealingWithGenericCommandErrorsReader$1(CommandCodecs.scala:19)
	at reactivemongo.play.json.JSONSerializationPack$.$anonfun$reader$1(JSONSerializationPack.scala:157)
	at play.api.libs.json.Reads$$anon$6.reads(Reads.scala:195)
	at reactivemongo.play.json.JSONSerializationPack$.deserialize(JSONSerializationPack.scala:59)
```

# Workarounds
The following combinations of dependency versions was tested to work okay:

## Option A
play-json-joda 2.6.13  
Play 2.7  
play2-reactivemongo 0.16.2-play27  

## Option B
play-json-joda 2.7.1  
Play 2.6.21  
play2-reactivemongo 0.16.2-play26  

# The Test
This sample project exposes 2 API endpoints:  
- `/save` - which saves a DateTime object to a MongoDB "test" collection in a `test` database
- `/load` - which loads the saved DateTime object and displays it

## Example 

To save the JODA DateTime object to DB: 
```
curl localhost:9000/save
```
should just return 200 OK

To load it back:
```
curl localhost:9000/load
```
which will return something like `2019-03-05T17:20:03.918-06:00` in plain text.


