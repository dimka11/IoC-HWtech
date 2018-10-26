# How to

To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
*maven:*
```xml
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
*gradle:*	
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2.** Add the dependency

*maven:*
```xml
<dependency>
	    <groupId>com.github.dimka11.IoC-HWtech</groupId>
	    <artifactId>IoC</artifactId>
	    <version>Release</version>
	</dependency>
```
*gradle:*
```groovy
dependencies {
	        implementation 'com.github.dimka11.IoC-HWtech:IoC:Release'
	}
```

