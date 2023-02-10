package cmd

import "github.com/spf13/cobra"

var (
	cfgFile     string
	userLicense string
	rootCmd     = &cobra.Command{
		Use:   "cobra-cli",
		Short: "A generator for Cobra based Applications",
		Long:  "",
	}
)