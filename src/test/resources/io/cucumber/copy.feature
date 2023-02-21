@copy
Feature: Copy item

  As a user
  I want to copy files between different locations of my account
  So that i have different copies in different places that i can modify separately

  Background: User is logged in
    Given user Alice is logged

  @smoke
  Scenario Outline: Copy an existent item to another location
    Given the following items have been created in the account
      | <type>   | <name>    |
      | folder   | Documents |
    When Alice selects to Copy the <type> <name>
    And Alice selects <space> as space and <target> as target folder
    Then Alice should see <name> in the filelist as original
    And Alice should see <name> inside the folder <target>

    Examples:
      | type     |  name          | target     | space     |
      | folder   |  Copyfolder    | Documents  | Personal  |
      | file     |  Copyfile.txt  | Documents  | Personal  |

  Scenario: Copy a folder to another place with same item name
    Given the following items have been created in the account
      | folder   | copy2        |
      | folder   | copy3        |
      | folder   | copy2/copy3  |
    When Alice selects to Copy the folder copy3
    And Alice selects Personal as space and copy2 as target folder
    Then Alice should see 'copy3 (2)' inside the folder copy2

  Scenario: Copy a folder to itself
    Given the following items have been created in the account
      | folder   | copy4  |
    When Alice selects to Copy the folder copy4
    And Alice selects Personal as space and copy4 as target folder
    Then Alice should see the following error
      | It is not possible to copy a folder into a descendant |