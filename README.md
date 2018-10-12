jcipher: Jasypt compatible encryption and decryption CLI
========================================================

[Jasypt](http://www.jasypt.org/) is a popular method in the
JVM world to perform symmetric cryptography. It is notably
used as method to secure key credentials in databases for
instance.

**jcipher** provides a command-line tool compatible with **Jasypt**'s
default parameters, optionally available as a standalone executable.

## Usage

```
Usage: jcipher [-e] -k KEY PAYLOAD

 Switches                     Default  Desc
 --------                     -------  ----
 -h, --no-help, --help        false    Show Help
 -e, --no-encrypt, --encrypt  false    Perform encryption
 -k, --key                             Key password
```

## Building

**jcipher** requires leiningen and can optionally be built as
a standalone executable.

To build a standalone JAR:

    lein uberjar
	
To build a standalone executable, you will need
[GraalVM](https://www.graalvm.org/) installed and the `GRAAL_HOME`
environment variable set to its location.

You can then run:

    lein native-image

## License

See [LICENSE](LICENSE) file.

Copyright © 2018 Pierre-Yves Ritschard <pyr@spootnik.org>

