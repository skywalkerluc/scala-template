output "role_arn" {
  value       = try(module.web.role_arn, null)
  description = "Role ARN"
}
output "ecr_repository_name" {
  value = module.web.ecr_name
  description = "Name of the ECR registry for REPO-NAME-CHANGE"
}

output "s3_bucket_arn" {
  value       = try(module.web.s3_bucket_arn, null)
  description = "Bucket ARN"
}

output "vault_arn" {
  value       = try(module.web.vault_arn, null)
  description = "Vault ARN"
}
