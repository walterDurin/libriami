## API Examples ##

### Access your LDIF contacts ###

```
// Create a buffered reader of sample file
BufferedReader in = new BufferedReader(new FileReader("tests/example1.ldif"));

// Create a LDIF decoder and parse the stream
AddressBook addressBook = DecoderFactory.getLdifDecoder().decode(in);
in.close();

// Dump contacts
System.out.println(addressBook);

// Use model to access data, i.e. mail address
String email = addressBook.getContacts().get(0).getEmail();
System.out.println("First contact has mail address: " + email);
```

### Sort your contacts regarding their birthday (who's next?) ###

```
// Decode ldif or vcard file
addressBook = decoder.decode(in, new ConsoleCoderListener());

// Sort them
Collections.sort(addressBook.getContacts(), new Comparator<Contact>() {
	public int compare(Contact o1, Contact o2) {
		Birthday b1 = o1.getBirthday();
		Birthday b2 = o2.getBirthday();
		if (b1 == null)
			return -1;
		if (b2 == null)
			return 1;

		Calendar bc1 = b1.getNextBirthday();
		Calendar bc2 = b2.getNextBirthday();
		return bc1.compareTo(bc2);
	}
});

// Now dump the contacts to STDOUT
for (Contact c : addressBook.getContacts()) {
	String s = c.toString();
	if (c.getBirthday() != null)
		System.out.println(s);
}
```