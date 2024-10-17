###### This is the one file that developers should change ########

###### The name of application
application_name     = "REPO-NAME-CHANGE"

###### Application namespace on Kubernetes          
namespace_identifier = "K8S-NAMESPACE-CHANGE"

####### Application squad Owner
business_owner       = "K8S-NAMESPACE-CHANGE"

####### Policies to alllow your application acess AWS resources
inline_policies      = {
      secret-manager = "secret_manager.json"
    }
            

