#### This file is used to declare the variables ####
####   ONLY DEVOPS TEAM CAN CHANGE THIS FILE    ####
#################################################### 

variable "environment" {
  type        = string
  default     = "production"
  description = "Application environment"
}

variable "region" {
  type        = string
  default     = "sa-east-1"
  description = "AWS Region"
}

variable "application_name" {
  type        = string
  default     = null
  description = "Application name"
}

variable "namespace_identifier" {
  type        = string
  default     = null
  description = "Application namespace"
}

variable "oidc_provider" {
  type        = string
  default     = "C82081EC5681DE96101CF719D669B202"
  description = "OIDC provider"
}

variable "create_vault" {
  type        = bool
  default     = true
  description = "Vault creation"
}

variable "create_identity" {
  type        = bool
  default     = true
  description = "Role creation"
}

variable "create_bucket" {
  type        = bool
  default     = false
  description = "Bucket creation"
}

variable "repository" {
  type        = string
  default     = null
  description = "GitHub Repository"
}

variable "application_identifier" {
  type        = string
  default     = null
  description = "Harness application ID"
}

variable "sample_secret" {
  type        = map(string)
  default     = {}
  description = "Sample da Secret"
}

variable "pipeline_web" {
  type        = bool
  default     = true
  description = "Pipeline Web"
}

variable "pipeline_consumer" {
  type        = bool
  default     = false
  description = "Pipeline consumer"
}

variable "inline_policies" {
  type        = map(string)
  default     = {}
  description = "Json inline policies"
}


variable "bucket_name" {
  type        = string
  default     = null
  description = "Nome do Bucket"
}

variable "business_owner" {
  type        = string
  default     = null
  description = "Application/Squad/Business Owner"
}

