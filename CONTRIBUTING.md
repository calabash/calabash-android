## Contributing

To avoid duplicates, please search existing issues before reporting a
new one.

Github Issues is intended for reporting bugs and feature suggestions. If
you have a question or need support, please use [Stack
Overflow](https://stackoverflow.com/questions/tagged/calabash) or join
the conversation on [Gitter](https://gitter.im/calabash/calabash0x?utm_source=share-link&utm_medium=link&utm_campaign=share-link).

## Etiquette and Code of Conduct

All contributors must adhere to the [Microsoft Open Source Code of
Conduct](https://opensource.microsoft.com/codeofconduct/). For more
information see the [Code of Conduct
FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact
opencode@microsoft.com with any additional questions or comments.

## Legal

Users who wish to contribute will be prompted to sign a Microsoft
Contributor License Agreement (CLA). A copy of the CLA can be found at
https://cla.microsoft.com/cladoc/microsoft-contribution-license-agreement.pdf.

Please consult the LICENSE file in this project for copyright and
license details.

## Releasing

```
$ cd path/to/calabash-android-server
$ git checkout develop
$ git pull
$ git fetch --tags
$ git checkout -b tag/1.2.3 1.2.3

$ cd path/calabash-android/ruby-gem
$ git checkout master
$ git pull
$ git checkout -b release/1.2.3

1. Bump the ruby version in lib/calabash-android/verison.rb
2. Build the TestServer.apk.
   $ rake build_server
3. Update the CHANGELOG.
4. Check the README for items to update.
5. Make pull request, get a review, and merge.

$ git checkout master
$ git pull
$ rake release

Announce the release on the public channels.
```
