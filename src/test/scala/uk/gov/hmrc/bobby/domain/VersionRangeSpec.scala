package uk.gov.hmrc.bobby.domain

import org.scalatest.{FlatSpec, Matchers}

class VersionRangeSpec extends FlatSpec with Matchers {


  "A VersionRange" should "read (,1.0] as 'x <= 1.0'" in {

    val r = VersionRange("(,1.0.0]")

    r.lowerBound shouldBe None
    r.lowerBoundInclusive shouldBe false
    r.upperBound shouldBe Some(Version("1.0.0"))
    r.upperBoundInclusive shouldBe true

  }

  it should "read [1.0.0] as fixed version '1.0.0'" in {

    val r = VersionRange("[1.0.0]")

    r.lowerBound shouldBe Some(Version("1.0.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe Some(Version("1.0.0"))
    r.upperBoundInclusive shouldBe true

  }

  it should "read [1.2.0,1.3.0] as 1.2.0 <= x <= 1.3.0" in {

    val r = VersionRange("[1.2.0,1.3.0]")

    r.lowerBound shouldBe Some(Version("1.2.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe Some(Version("1.3.0"))
    r.upperBoundInclusive shouldBe true

  }

  it should "read [1.0.0,2.0.0) as 1.0.0 <= x < 2.0.0" in {

    val r = VersionRange("[1.0.0,2.0.0)")

    r.lowerBound shouldBe Some(Version("1.0.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe Some(Version("2.0.0"))
    r.upperBoundInclusive shouldBe false

  }

  it should "read [8.0.0,8.4.1] as 8.0.0 <= x <= 8.4.1" in {

    val r = VersionRange("[8.0.0,8.4.1]")

    r.lowerBound shouldBe Some(Version("8.0.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe Some(Version("8.4.1"))
    r.upperBoundInclusive shouldBe true

  }

  it should "read ranges with spaces" in {

    val r = VersionRange("[8.0.0, 8.4.1]")

    r.lowerBound shouldBe Some(Version("8.0.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe Some(Version("8.4.1"))
    r.upperBoundInclusive shouldBe true

  }

  it should "read [1.5.0,) as x >= 1.5.0" in {

    val r = VersionRange("[1.5.0,)")

    r.lowerBound shouldBe Some(Version("1.5.0"))
    r.lowerBoundInclusive shouldBe true
    r.upperBound shouldBe None
    r.upperBoundInclusive shouldBe false

  }

  it should "throw an IllegalArgumentException when incomplete version is provided" in {
    an[IllegalArgumentException] should be thrownBy VersionRange("[1.5,)")
  }

  it should "throw an IllegalArgumentException when brackets are missing" in {
    an[IllegalArgumentException] should be thrownBy VersionRange("1.5,)")
    an[IllegalArgumentException] should be thrownBy VersionRange("[1.5,")
    an[IllegalArgumentException] should be thrownBy VersionRange("1.5")
  }

  it should "throw an IllegalArgumentException when the range is open on both sides" in {
    an[IllegalArgumentException] should be thrownBy VersionRange("(,1.5,)")
  }

  it should "throw an IllegalArgumentException when multiple sets are used" in {
    an[IllegalArgumentException] should be thrownBy VersionRange("(,1.0],[1.2,)")
  }

  it should "include 1.2.5 when the expression is [1.2.0,1.3.0]" in {
    VersionRange("[1.2.0,1.3.0]").includes(Version("1.2.5")) shouldBe true
  }
  it should "include 0.2.0 when the expression is (,1.0.0]" in {
    VersionRange("(,1.0.0]").includes(Version("0.2.0")) shouldBe true
  }
  it should "include 1.2.0 when the expression is [1.0.0,)" in {
    VersionRange("[1.0.0,)").includes(Version("1.2.0")) shouldBe true
  }
  it should "not include the left boundary when the expression is (1.0.0,)" in {
    VersionRange("(1.0.0,)").includes(Version("1.0.0")) shouldBe false
  }
  it should "not include the right boundary when the expression is (,1.0.0)" in {
    VersionRange("(,1.0.0)").includes(Version("1.0.0")) shouldBe false
  }
}
