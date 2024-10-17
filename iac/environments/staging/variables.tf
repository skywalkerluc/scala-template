#### This file is used to declare the variables ####
####   ONLY DEVOPS TEAM CAN CHANGE THIS FILE    ####
#################################################### 

variable "environment" {
  type        = string
  default     = "staging"
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
  default     = "9475C3238BF855326376EF52FBE4ED42"
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

variable "create_ecr" {
  type        = bool
  default     = true
  description = "ECR creation"
}

variable "create_bucket" {
  type        = bool
  default     = false
  description = "Bucket creation"
}

variable "sample_secret" {
  type        = map(string)
  default     = {}
  description = "Sample da Secret"
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
