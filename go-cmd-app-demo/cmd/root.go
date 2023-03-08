package cmd

import (
	"fmt"
	"github.com/spf13/cobra"
	"github.com/spf13/viper"
	"os"
)

var (
	// used for flags
	cfgFile     string
	userLicense string
	author      *string
	rootCmd     = &cobra.Command{
		Use:   "cobra-cli",
		Short: "A generator for Cobra based Applications",
		Long: `Cobra is a CLI library for Go that empowers applications.
This application is a tool to generate the needed files
to quickly create a Cobra application.`,
		Run: func(cmd *cobra.Command, args []string) {
			fmt.Println(cfgFile, userLicense)
			fmt.Println(*author)
		},
	}
)

func Execute() error {
	return rootCmd.Execute()
}

func initConfig() {
	if cfgFile != "" {
		// use config file form flag
		viper.SetConfigFile(cfgFile)
	} else {
		// find home dir
		home, err := os.UserHomeDir()
		cobra.CheckErr(err)

		// search config in home dir with name .cobra
		viper.AddConfigPath(home)
		viper.SetConfigType("yaml")
		viper.SetConfigName(".cobra")
	}
	viper.AutomaticEnv()

	if err := viper.ReadInConfig(); err != nil {
		fmt.Println("Using config file:", viper.ConfigFileUsed())
	}
}

func init() {
	cobra.OnInitialize()
	// cobra-cli --config file
	rootCmd.PersistentFlags().StringVar(&cfgFile, "config", "", "config file (default is $HOME/.cobra.yaml)")
	// cobra-cli -a / --author
	author = rootCmd.PersistentFlags().StringP("author", "a", "YOUR NAME", "author name for copyright attribution")
	// cobra-cli -l / --license
	rootCmd.PersistentFlags().StringVarP(&userLicense, "License", "l", "", "name of license for project")
	// cobra-cli --viper
	rootCmd.PersistentFlags().Bool("viper", true, "use Viper for configuration")
	//
	viper.BindPFlag("author", rootCmd.PersistentFlags().Lookup("author"))
}
