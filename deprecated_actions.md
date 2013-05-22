Deprecation of actions that can be implemented using Ruby API
=============================================================

To clean up the API and the test server I plan to remove all the actions that can easily be implemented using the Ruby API.
So far the actions has been very poorly documented and working inconsistently because they have been implemented over time, by different people and started out as a mapping to Robotium methods.
Predefined steps that are using a deprecated action will be re-implemented using the Ruby API. Some predefined steps might be removed during this cleanup.

I think this will inevitable break backwards compatibility but we should try to make the impact minimal.

The cleanup will be done in two phases each will bump the minor version. I imagine it will be executed like this:

* **0.5.x**
    1. A number of actions will be flagged as deprecated and a warning will be printed every time one of these actions are invoked.
    2. All predefined step that use the deprecated actions will be reimplemented using the Ruby API.
    3. Predefined steps that does not "feel right" will be marked as deprecated. Predefined steps are for beginners only and should be easy to use and not support complex interactions or verifications.

* **0.6.x**
    1. Deprecated actions will be removed.
    2. Deprecated steps will be removed.

Please comment below if you have any opinion to this plan.