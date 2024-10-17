terraform {
  required_version = ">= 1.4"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 4.9"
    }
  }

  backend "s3" {
    bucket  = "caju-devops-archive"
    key     = "atlantis/state/production/REPO-NAME-CHANGE"
    region  = "sa-east-1"
    encrypt = true
  }
}

provider "aws" {
  region = var.region

  default_tags {
    tags = {
      environment = var.environment
      application = var.application_name
    }
  }
}
