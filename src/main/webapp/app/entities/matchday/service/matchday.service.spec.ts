import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatchday, Matchday } from '../matchday.model';

import { MatchdayService } from './matchday.service';

describe('Matchday Service', () => {
  let service: MatchdayService;
  let httpMock: HttpTestingController;
  let elemDefault: IMatchday;
  let expectedResult: IMatchday | IMatchday[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatchdayService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      graphicsName: 'AAAAAAA',
      number: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Matchday', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Matchday()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Matchday', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          graphicsName: 'BBBBBB',
          number: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Matchday', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          graphicsName: 'BBBBBB',
        },
        new Matchday()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Matchday', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          graphicsName: 'BBBBBB',
          number: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Matchday', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMatchdayToCollectionIfMissing', () => {
      it('should add a Matchday to an empty array', () => {
        const matchday: IMatchday = { id: 123 };
        expectedResult = service.addMatchdayToCollectionIfMissing([], matchday);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchday);
      });

      it('should not add a Matchday to an array that contains it', () => {
        const matchday: IMatchday = { id: 123 };
        const matchdayCollection: IMatchday[] = [
          {
            ...matchday,
          },
          { id: 456 },
        ];
        expectedResult = service.addMatchdayToCollectionIfMissing(matchdayCollection, matchday);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Matchday to an array that doesn't contain it", () => {
        const matchday: IMatchday = { id: 123 };
        const matchdayCollection: IMatchday[] = [{ id: 456 }];
        expectedResult = service.addMatchdayToCollectionIfMissing(matchdayCollection, matchday);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchday);
      });

      it('should add only unique Matchday to an array', () => {
        const matchdayArray: IMatchday[] = [{ id: 123 }, { id: 456 }, { id: 4915 }];
        const matchdayCollection: IMatchday[] = [{ id: 123 }];
        expectedResult = service.addMatchdayToCollectionIfMissing(matchdayCollection, ...matchdayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const matchday: IMatchday = { id: 123 };
        const matchday2: IMatchday = { id: 456 };
        expectedResult = service.addMatchdayToCollectionIfMissing([], matchday, matchday2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchday);
        expect(expectedResult).toContain(matchday2);
      });

      it('should accept null and undefined values', () => {
        const matchday: IMatchday = { id: 123 };
        expectedResult = service.addMatchdayToCollectionIfMissing([], null, matchday, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchday);
      });

      it('should return initial array if no Matchday is added', () => {
        const matchdayCollection: IMatchday[] = [{ id: 123 }];
        expectedResult = service.addMatchdayToCollectionIfMissing(matchdayCollection, undefined, null);
        expect(expectedResult).toEqual(matchdayCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
