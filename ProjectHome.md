For developers that hate J2EE, JSP, Java Faces or Tapestry. J2mvc let's you code fast and deliver better web applications.

No XML, minimal configuration, play everywhere implementation.

This is still in early development stages.

# Introduction #

Jmvc is a lightweight web framework built on java. It is based on the View-Model-Controller pattern and currently supports only MySQL. Jmvc was created as a substitute to all those complicated, inflexible frameworks that are currently in fashion in the Java world.


# Details #

Jmvc was inspired by Code Igniter, a lightweight, small, powerful PHP framework and Django, the python web framework. If you have done development in any of those frameworks and you understand a bit of Java then you'll be at home with jmvc.

Why use it?

  * Jmvc was designed to help you concentrate on coding for your application and not for your framework. No configuration, no XML, no nothing. Get in and start coding.
  * Jmvc helps you separate concerns. Application logic, data retrieval and design are done in totally separate places in your code. That means you have no jdbc statements in you logic or code in your html.
  * Repetitive tasks like pagination, session management, jdbc handling are simplified by built-in libraries.
  * No J2EE, no beans, no java weirdness whatsoever. Your code will be short and to the point. Just plain old java.
  * Extensive database abstraction layer. Jmvc supports both Active Record & Query sets. If you want to retrieve a blog post from the database you only need to write post.load(post\_id).
  * It's extensible. You can write your own libraries either on top of those provided or roll your own.
  * It's open, no fees, no charges & you can take peek at the code.