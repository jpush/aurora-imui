
import Foundation

public extension Date {
  
  public func plus(seconds s: UInt) -> Date {
    return self.addComponentsToDate(seconds: Int(s), minutes: 0, hours: 0, days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func minus(seconds s: UInt) -> Date {
    return self.addComponentsToDate(seconds: -Int(s), minutes: 0, hours: 0, days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func plus(minutes m: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: Int(m), hours: 0, days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func minus(minutes m: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: -Int(m), hours: 0, days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func plus(hours h: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: Int(h), days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func minus(hours h: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: -Int(h), days: 0, weeks: 0, months: 0, years: 0)
  }
  
  public func plus(days d: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: Int(d), weeks: 0, months: 0, years: 0)
  }
  
  public func minus(days d: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: -Int(d), weeks: 0, months: 0, years: 0)
  }
  
  public func plus(weeks w: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: Int(w), months: 0, years: 0)
  }
  
  public func minus(weeks w: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: -Int(w), months: 0, years: 0)
  }
  
  public func plus(months m: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: 0, months: Int(m), years: 0)
  }
  
  public func minus(months m: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: 0, months: -Int(m), years: 0)
  }
  
  public func plus(years y: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: 0, months: 0, years: Int(y))
  }
  
  public func minus(years y: UInt) -> Date {
    return self.addComponentsToDate(seconds: 0, minutes: 0, hours: 0, days: 0, weeks: 0, months: 0, years: -Int(y))
  }
  
  fileprivate func addComponentsToDate(seconds sec: Int, minutes min: Int, hours hrs: Int, days d: Int, weeks wks: Int, months mts: Int, years yrs: Int) -> Date {
    var dc = DateComponents()
    dc.second = sec
    dc.minute = min
    dc.hour = hrs
    dc.day = d
    dc.weekOfYear = wks
    dc.month = mts
    dc.year = yrs
    return Calendar.current.date(byAdding: dc, to: self)!
  }
  
  public func midnightUTCDate() -> Date {
    var dc: DateComponents = Calendar.current.dateComponents([.year, .month, .day], from: self)
    dc.hour = 0
    dc.minute = 0
    dc.second = 0
    dc.nanosecond = 0
    dc.timeZone = TimeZone(secondsFromGMT: 0)
    return Calendar.current.date(from: dc)!
  }
  
  public static func secondsBetween(date1 d1:Date, date2 d2:Date) -> Int {
    let dc = Calendar.current.dateComponents([.second], from: d1, to: d2)
    return dc.second!
  }
  
  public static func minutesBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.minute], from: d1, to: d2)
    return dc.minute!
  }
  
  public static func hoursBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.hour], from: d1, to: d2)
    return dc.hour!
  }
  
  public static func daysBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.day], from: d1, to: d2)
    return dc.day!
  }
  
  public static func weeksBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.weekOfYear], from: d1, to: d2)
    return dc.weekOfYear!
  }
  
  public static func monthsBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.month], from: d1, to: d2)
    return dc.month!
  }
  
  public static func yearsBetween(date1 d1: Date, date2 d2: Date) -> Int {
    let dc = Calendar.current.dateComponents([.year], from: d1, to: d2)
    return dc.year!
  }
  
  //MARK- Comparison Methods
  
  public func isGreaterThan(_ date: Date) -> Bool {
    return (self.compare(date) == .orderedDescending)
  }
  
  public func isLessThan(_ date: Date) -> Bool {
    return (self.compare(date) == .orderedAscending)
  }
  
  //MARK- Computed Properties
  
  public var day: UInt {
    return UInt(Calendar.current.component(.day, from: self))
  }
  
  public var month: UInt {
    return UInt(NSCalendar.current.component(.month, from: self))
  }
  
  public var year: UInt {
    return UInt(NSCalendar.current.component(.year, from: self))
  }
  
  public var hour: UInt {
    return UInt(NSCalendar.current.component(.hour, from: self))
  }
  
  public var minute: UInt {
    return UInt(NSCalendar.current.component(.minute, from: self))
  }
  
  public var second: UInt {
    return UInt(NSCalendar.current.component(.second, from: self))
  }
  
  // to YYYY-MM-DD-hh-mm-ss
  public var parseDate: String {
    return "\(year)-\(month)-\(day) \(hour):\(minute):\(second)"
  }
}
