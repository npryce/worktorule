Work to Rule
============

[![Build Status](https://travis-ci.org/npryce/worktorule.svg)](https://travis-ci.org/npryce/worktorule)

Manage test lifecycle by correlating failures with contents of an issue tracker.

Work to Rule lets you mark tests as in progress and associate them with issues in an issue tracker.  Work to Rule
will report failing, in progress tests as skipped while the issue is open, and report failures as normal if they
reoccur after the issue is closed.

This lets you follow an acceptance-test driven development process 
(as described in [Growing Object Oriented Software, Developed by Tests](http://www.growing-object-oriented-software.com) 
and other books) in which you start by writing acceptance tests that fail, and then implement the functionality with 
(Unit) TDD until the acceptance tests pass.

It is also useful for associating contract tests that detect failures in third-party libraries or services with
issues in the vendor's issue tracker.

Requires:

 - Java (1.6+)
 - JUnit

Available in Maven Central as [com.natpryce:worktorule:\<version\>](http://search.maven.org/#browse%7C542918654)


Quick Start
-----------

Let's say you have a failing acceptance test.

~~~~~~~~~~~~~~~~~~~~~java
public class ExampleAcceptanceTest {
    @Test
    public void everything_is_awesome() {
        assertThat(everything.awesomeness(), equalTo(1.0));
    }
}
~~~~~~~~~~~~~~~~~~~~~

You want to check that in to your project, but you don't want it to break the build while you're working on the feature. At this stage, the test is used for tracking development, not detecting regressions.

First, add the [IgnoreInProgress](src/main/java/com/natpryce/worktorule/IgnoreInProgress.java) rule to your test.  The rule's constructor takes an instance of an [IssueTracker](src/main/java/com/natpryce/worktorule/IssueTracker.java) that can query the state of issues for your project.  In this example, our project uses a public GitHub repository.

~~~~~~~~~~~~~~~~~~~~~java
import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.issues.github.GitHubIssues;

public class ExampleAcceptanceTest {
    @Rule public TestRule ignoreInProgressTests = new IgnoreInProgress(
        new GitHubIssues("example-organisation", "example-project"));

    @Test
    public void everything_is_awesome() {
        assertThat(everything.awesomeness(), equalTo(1.0));
    }
}
~~~~~~~~~~~~~~~~~~~~~

Then, annotate the failing test as [InProgress](src/main/java/com/natpryce/worktorule/InProgress.java) and associate it with one or more issues in your issue tracker by passing the issue IDs  as parameters to the InProgress annotation. 

~~~~~~~~~~~~~~~~~~~~~java
import com.natpryce.worktorule.IgnoreInProgress;
import com.natpryce.worktorule.InProgress;
import com.natpryce.worktorule.issues.github.GitHubIssues;

public class ExampleAcceptanceTest {
    @Rule public TestRule ignoreInProgressTests = new IgnoreInProgress(
        new GitHubIssues("example-organisation", "example-project"));

    @Test
    @InProgress("1")
    public void everything_is_awesome() {
        assertThat(everything.awesomeness(), equalTo(1.0));
    }
}
~~~~~~~~~~~~~~~~~~~~~

That's it.

What next?
----------

You'll probably want to define a constant or factory method for the IssueTracker so you don't have to duplicate 
connection parameters across your test code. 

~~~~~~~~~~~~~~~~~~~~~java
public class DevelopmentEnvironment {
    public static IssueTracker issueTracker = new GitHubIssues("example-organisation", "example-project");
    
    ....
}

public class ExampleAcceptanceTest {
    @Rule public TestRule ignoreInProgressTests = new IgnoreInProgress(DevelopmentEnvironment.issueTracker);
    
    ...
}
~~~~~~~~~~~~~~~~~~~~~


Supported Issue Trackers
------------------------

Work to Rule supports several popular issue trackers out of the box:

 - [GitHub Issues](https://guides.github.com/features/issues/)
 - [Bitbucket Issues](https://confluence.atlassian.com/display/BITBUCKET/Use+the+issue+tracker)
 - [JIRA](https://www.atlassian.com/software/jira)
 - [Trello](https://trello.com/)

It is easy to plug in support for new issue trackers if your preferred issue tracker is not one of the above. 
Contributions welcome!
