import sbt.Def
import sbtghpackages.GitHubPackagesPlugin.autoImport.{
  TokenSource,
  githubTokenSource
}

object GithubPackages {

  /** The plugin "sbt-github-packages" requires a GITHUB_TOKEN environment variable in order to be able to fetch the hosted artifact.
    * The problem is that IntelliJ cannot load projects with this plugin because it offers no way of defining environment variable to SBT when loading a project.
    * A solution is to add "sbt-dotenv" plugin and before the project is loaded, create an .env file with the required environment variable.
    * This solution works like a charm since "sbt-dotenv" could be reused to set all application environment variables as well.
    *
    * If you can't adopt the solution described above for any reason, the following solution is for you!
    * First, try to retrieve from environment, if no success, try to retrieve from ~/.gitconfig file under user.token key
    * If you are curious about this topic, please check [this](https://github.com/djspiewak/sbt-github-packages/issues/24) issue
    * If you are curious about why this setting is repeated across all sbt modules, please check [this](https://github.com/djspiewak/sbt-github-packages/pull/34#issuecomment-786462153)
    */
  val tokenResolutionSource: Def.Setting[TokenSource] =
    githubTokenSource := TokenSource.Environment("GITHUB_TOKEN") || TokenSource
      .GitConfig("user.token")

}
