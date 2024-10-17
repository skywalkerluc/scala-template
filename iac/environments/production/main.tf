module "web" {

  source = "git@github.com:caju-beneficios/terraform-md-backend.git"

  environment          = var.environment
  application_name     = var.application_name
  namespace_identifier = var.namespace_identifier
  repository           = var.application_name
  inline_policies      = var.inline_policies
  create_bucket        = var.create_bucket
  business_owner       = var.business_owner 
  oidc_provider        = var.oidc_provider
}
