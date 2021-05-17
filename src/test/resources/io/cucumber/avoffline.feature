@avoffline
Feature: Set items as available offline (downloaded and synced)

  As a user
  I want to set content as available offline
  So that the content will be always downloaded and synced

  Background: User is logged in
    Given user user1 is logged

  Scenario: Set a file as available offline
    Given the file ownCloud Manual.pdf has been created in the account
    When user selects to Set as available offline the item ownCloud Manual.pdf
    Then user should see the item ownCloud Manual.pdf as av.offline
    And the item ownCloud Manual.pdf should be stored in the device