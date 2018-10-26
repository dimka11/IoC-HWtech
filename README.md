#h1 **How to**

To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	```
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency
```
<dependency>
	    <groupId>com.github.dimka11.IoC-HWtech</groupId>
	    <artifactId>IoC</artifactId>
	    <version>Release</version>
	</dependency>
	```
```
dependencies {
	        implementation 'com.github.dimka11.IoC-HWtech:IoC:Release'
	}
	```
