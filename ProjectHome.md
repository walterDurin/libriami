The libriami Java API provides access to different addressbook formats (as ldif and vcard), removes duplicates automatically and exports the unified addressbook to one of the supported file formats.

You can either use the API in your Java code or as a simple commandline tool to manage your contacts.

Features of libriami

  * Include libriami in your Java application and extend it ([API](API.md))
  * Collect address contacts from different sources ([Tool](Tool.md))
  * Get rid of duplicates and merge contact information automatically ([Tool](Tool.md))
  * Auto-fix typical errors ([Tool](Tool.md))

Supported file formats

  * LDIF (used by Thunderbird, LDAP servers, etc.) - See [RFC2849](http://tools.ietf.org/html/rfc2849) for details
  * VCARD (used by MS and Apple products) - See [vCard specification](http://datatracker.ietf.org/doc/draft-ietf-vcarddav-vcardrev/) for details

Usecases

  * You want to merge several addressbooks to a new one with merged contact information of the source files
  * You want to convert addressbooks between LDIF and vCard format
  * You want a convenient API to access LDIF or vCard files
