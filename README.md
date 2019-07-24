# Injected provider states example

This is an example of a consumer and provider pact test where the provider states values can be injected into the test.

It has two sub-projects: the consumer and provider. Both are Springboot applications.

## Consumer

For our example, our consumer is a springboot app that is going to create some kind of transaction for an account.
We'll create a `/new-transaction` endpoint to kick this flow off. It will then have to call out to the provider to
get the account data.

## Provider

The provider is a springboot app that has an `account` resource. The consumer app will call this to create a new account
as part of creating a new transaction.
