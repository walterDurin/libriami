### Display contacts of an LDIF/vCard file ###
Specify any supported file as argument and by default the contact information is being displayed in a special compact format.
```
java -jar libriami.jar myaddressbook.ldif
```

### Convert contacts from one file format to the other (LDIF -> vCard) ###
If you want to display the result in a specific format, you can define the output format.
```
java -jar libriami.jar -f vcard myaddressbook.ldif > myaddressbook.vcf
```

### Merge two addressbook files and fix duplicates automatically ###
You can pass multiple source files as commandline arguments. All of them are being merged together. Often duplicate entries of same contacts will exist in your output. Libriami can try to detect these duplicates and merge all information automatically.

```
java -jar libriami.jar -df ldif outlookexport.vcf thunderbirdexport.ldif > mycontacts.ldif
```