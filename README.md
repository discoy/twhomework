# twhomework
the thoughtworks homework wechat like

The WeChat Moments(Android)
Overview
The assignment is to build an Android App which looks like WeChat Moments page. Production and Technical requirements are weighing equally in final result.
Production requirements
• The view consists of profile image, avatar and tweets list
• For each tweet, there will be a sender, optional content, optional images and comments, ignore the tweet which does not contain a content and images
• A tweet contains from 0 to 9 images, make sure image group layout looks like WeChat style
• Load all tweets in memory at first time, and get 5 of them each time from memory asynchronously
• Show 5 more while user pulling up the view at the bottom of view
• Pulling down the view to refresh, only first 5 items are shown after refreshing
• The layout in different Android devices(except tablet) should be considered
• This is a static page, no more actions(tapping, multi-touching) are required. Any extra interaction is welcome and will be serious considered in a positive way. For any other unclear layout concerns, please reference current WeChat implementation.
Technical requirements
Target should be API 16+
Git for source control, clear git history is required Good code architecture is preferred
3rd lib for json parser is preferred
Custom implementation of image loader is preferred
Disk cache is preferred
Keep code clean as much as possible Unit tests is appreciated
Json data specification
• Request user info from url: http://thoughtworks-ios.herokuapp.com/user/jsmith
• Request tweets from url: http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets
