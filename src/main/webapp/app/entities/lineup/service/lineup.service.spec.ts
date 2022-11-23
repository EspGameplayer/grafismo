import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILineup, Lineup } from '../lineup.model';

import { LineupService } from './lineup.service';

describe('Lineup Service', () => {
  let service: LineupService;
  let httpMock: HttpTestingController;
  let elemDefault: ILineup;
  let expectedResult: ILineup | ILineup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LineupService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a Lineup', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Lineup()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lineup', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lineup', () => {
      const patchObject = Object.assign({}, new Lineup());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lineup', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a Lineup', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLineupToCollectionIfMissing', () => {
      it('should add a Lineup to an empty array', () => {
        const lineup: ILineup = { id: 123 };
        expectedResult = service.addLineupToCollectionIfMissing([], lineup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lineup);
      });

      it('should not add a Lineup to an array that contains it', () => {
        const lineup: ILineup = { id: 123 };
        const lineupCollection: ILineup[] = [
          {
            ...lineup,
          },
          { id: 456 },
        ];
        expectedResult = service.addLineupToCollectionIfMissing(lineupCollection, lineup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lineup to an array that doesn't contain it", () => {
        const lineup: ILineup = { id: 123 };
        const lineupCollection: ILineup[] = [{ id: 456 }];
        expectedResult = service.addLineupToCollectionIfMissing(lineupCollection, lineup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lineup);
      });

      it('should add only unique Lineup to an array', () => {
        const lineupArray: ILineup[] = [{ id: 123 }, { id: 456 }, { id: 47412 }];
        const lineupCollection: ILineup[] = [{ id: 123 }];
        expectedResult = service.addLineupToCollectionIfMissing(lineupCollection, ...lineupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lineup: ILineup = { id: 123 };
        const lineup2: ILineup = { id: 456 };
        expectedResult = service.addLineupToCollectionIfMissing([], lineup, lineup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lineup);
        expect(expectedResult).toContain(lineup2);
      });

      it('should accept null and undefined values', () => {
        const lineup: ILineup = { id: 123 };
        expectedResult = service.addLineupToCollectionIfMissing([], null, lineup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lineup);
      });

      it('should return initial array if no Lineup is added', () => {
        const lineupCollection: ILineup[] = [{ id: 123 }];
        expectedResult = service.addLineupToCollectionIfMissing(lineupCollection, undefined, null);
        expect(expectedResult).toEqual(lineupCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
