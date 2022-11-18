# Twine [WIP?]

App to unroll Twitter threads. Built using Kotlin & Compose

## Setup

Requires Android Studio Dolphin or above to build

### Configuring tokens

Twine uses Twitter v2 API, in order to build and use the project you need to create a project at
[Twitter developer portal](https://developer.twitter.com/en/portal/dashboard).

Once the project is setup and approved (if anyone at Twitter still exists to do this).
Create prod and dev apps in the project. After that, create these system env variables and paste
appropriate tokens. You can find these tokens
from `Project & Apps > "Project" > "App" > Keys and tokens`

```
// For integration tests
TWINE_DEV_BEARER_TOKEN=""

TWINE_DEV_CLIENT_ID=""
TWINE_DEV_CLIENT_SECRET=""

TWINE_PROD_CLIENT_ID=""
TWINE_PROD_CLIENT_SECRET=""
```

## TODO

- [x] Login
- [ ] Home (Couple of things pending)
- [ ] Detail view
- [x] Settings
- [ ] Search
- [ ] Word filtering
- [ ] Alerts

## Made by

- [Sasikanth Miriyampalli](https://www.sasikanth.dev) / Development
- [Eduardo Pratti](https://twitter.com/edpratti) / Design

## License

```
Copyright 2022 Sasikanth Miriyampalli

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
