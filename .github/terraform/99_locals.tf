locals {
  # Common Tags:
  common_tags = {
    CreatedBy   = "Terraform"
    Environment = var.env
    Owner       = upper(var.prefix)
    Source      = "https://github.com/pagopa/p4pa-mocks" # Repository URL
    CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
  }

  # Repo
  github = {
    org        = "pagopa"
    repository = "p4pa-mocks" # Repository Name
  }

  env_secrets   = {}
  env_variables = {}

  repo_secrets = var.env_short == "p" ? {
    ADMIN_GITHUB_TOKEN_RW = data.azurerm_key_vault_secret.github_token[0].value
    AZURE_DEVOPS_TOKEN    = data.azurerm_key_vault_secret.azure_devops_token[0].value
  } : {}

  repo_env = {}

  map_repo = {
    "dev" : "*",
    "uat" : "uat"
    "prod" : "main"
  }
}
