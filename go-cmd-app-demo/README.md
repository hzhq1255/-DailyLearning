# go-cmd-app-demo
> learning `github.com/spf13/cobra` and use it complete demo


## 1 time show demo

want `time show` to show current time.

- **main command**

`root.go`

init time main command

```go
var rootCmd = &cobra.Command{
Use:   "hugo",
Short: "Hugo is a very fast static generator",
Long: `A longer description that spans multiple lines and likely contains
examples and usage of using your application. For example:

Cobra is a CLI library for Go that empowers applications.
This application is a tool to generate the needed files
to quickly create a Cobra application.`,
// Uncomment the following line if your bare application
// has an action associated with it:
Run: func(cmd *cobra.Command, args []string) {},
}

func Execute()  {
  err := rootCmd.Execute()
  if(err != nil){
	os.Exit(1)
  }
}

```

-  
`show.go`

```go
var showCmd = &cobra.Command{
Use:   "show",
Short: "Show current time",
Long: `A longer description that spans multiple lines and likely contains examples
and usage of using your command. For example:

Cobra is a CLI library for Go that empowers applications.
This application is a tool to generate the needed files
to quickly create a Cobra application.`,
Run: func(cmd *cobra.Command, args []string) {
fmt.Println(time.Now())
},
}
func init(){
	rootCmd.AddCommand(showCmd)
}
```
